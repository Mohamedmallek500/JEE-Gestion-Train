package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Reservation;
import model.User;
import util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import dao.ReservationDAO;

@WebServlet("/telechargerBillet")
public class TelechargerBilletServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TelechargerBilletServlet.class.getName());
    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connect� lors de l'acc�s � /telechargerBillet");
            session = request.getSession(true); // Cr�er une nouvelle session si n�cessaire
            session.setAttribute("error", "Veuillez vous connecter pour t�l�charger un billet.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reservationId = request.getParameter("id");
        if (reservationId == null || reservationId.trim().isEmpty()) {
            LOGGER.warning("ID de r�servation manquant lors de l'acc�s � /telechargerBillet");
            session.setAttribute("error", "ID de r�servation manquant.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("R�servation non trouv�e pour l'ID: " + reservationId);
                session.setAttribute("error", "R�servation non trouv�e.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            if (!"achet�".equals(reservation.getEtat())) {
                LOGGER.warning("R�servation non t�l�chargeable (�tat: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Seules les r�servations achet�es peuvent �tre t�l�charg�es.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            User user = (User) session.getAttribute("user");
            LOGGER.info("User en session: " + user.getId());
            LOGGER.info("User de la r�servation: " + reservation.getUser().getId());
            // Autoriser les admins ou le propri�taire de la r�servation
            if (!reservation.getUser().getId().equals(user.getId()) && !"admin".equals(user.getRole())) {
                LOGGER.warning("Utilisateur non autoris� � t�l�charger la r�servation ID: " + reservationId);
                session.setAttribute("error", "Vous n'�tes pas autoris� � t�l�charger ce billet.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            LOGGER.info("G�n�ration du PDF pour la r�servation ID: " + reservationId);
            generatePDF(reservation, response);
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de r�servation invalide.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la g�n�ration du PDF: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la g�n�ration du PDF: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/monCompte");
        }
    }

    private void generatePDF(Reservation reservation, HttpServletResponse response) throws IOException {
        // D�finir le type de contenu et l'en-t�te pour le t�l�chargement
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=billet_" + reservation.getId() + ".pdf");

        // Ajouter un log pour confirmer que la m�thode est appel�e
        LOGGER.info("D�but de la g�n�ration du PDF pour la r�servation ID: " + reservation.getId());

        // Cr�er un document PDF avec PDFBox
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.beginText();
                contentStream.newLineAtOffset(200, 750); // Centrer le titre
                contentStream.showText("Billet de Voyage");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("ID R�servation : " + reservation.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(50, 680);
                contentStream.showText("Utilisateur : " + reservation.getUser().getEmail());
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 650);
                contentStream.showText("D�tails du voyage :");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                float yPosition = 620;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Gare de d�part : " + reservation.getVoyage().getTrajet().getDepartStation().getName() + " (" + reservation.getVoyage().getTrajet().getDepartStation().getCity() + ")");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Gare d'arriv�e : " + reservation.getVoyage().getTrajet().getArrivalStation().getName() + " (" + reservation.getVoyage().getTrajet().getArrivalStation().getCity() + ")");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Heure de d�part : " + reservation.getVoyage().getHeureDepartFormatted());
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Heure d'arriv�e : " + reservation.getVoyage().getHeureArriveeFormatted());
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Dur�e : " + reservation.getVoyage().getDuree());
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Prix : " + reservation.getVoyage().getPrix() + " �");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Classe : " + reservation.getClasse());
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Pr�f�rences : " + (reservation.getPreferences() != null ? reservation.getPreferences() : "Aucune"));
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Date de r�servation : " + reservation.getDateReservation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                contentStream.endText();
                yPosition -= 20;

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("�tat : " + reservation.getEtat());
                contentStream.endText();
                yPosition -= 20;

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Date : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                contentStream.endText();
            }

            // �crire le document dans la r�ponse
            LOGGER.info("�criture du PDF dans la r�ponse pour la r�servation ID: " + reservation.getId());
            response.getOutputStream().flush(); // S'assurer que le flux est vid� avant d'�crire
            document.save(response.getOutputStream());
            response.getOutputStream().flush(); // S'assurer que tout est envoy� au client
            LOGGER.info("PDF g�n�r� et envoy� avec succ�s pour la r�servation ID: " + reservation.getId());
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'�criture du PDF: " + e.getMessage());
            throw new IOException("Erreur lors de l'�criture du PDF", e);
        }
    }
}