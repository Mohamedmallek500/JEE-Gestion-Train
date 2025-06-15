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
            LOGGER.warning("Utilisateur non connecté lors de l'accès à /telechargerBillet");
            session = request.getSession(true); // Créer une nouvelle session si nécessaire
            session.setAttribute("error", "Veuillez vous connecter pour télécharger un billet.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reservationId = request.getParameter("id");
        if (reservationId == null || reservationId.trim().isEmpty()) {
            LOGGER.warning("ID de réservation manquant lors de l'accès à /telechargerBillet");
            session.setAttribute("error", "ID de réservation manquant.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("Réservation non trouvée pour l'ID: " + reservationId);
                session.setAttribute("error", "Réservation non trouvée.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            if (!"acheté".equals(reservation.getEtat())) {
                LOGGER.warning("Réservation non téléchargeable (état: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Seules les réservations achetées peuvent être téléchargées.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            User user = (User) session.getAttribute("user");
            LOGGER.info("User en session: " + user.getId());
            LOGGER.info("User de la réservation: " + reservation.getUser().getId());
            // Autoriser les admins ou le propriétaire de la réservation
            if (!reservation.getUser().getId().equals(user.getId()) && !"admin".equals(user.getRole())) {
                LOGGER.warning("Utilisateur non autorisé à télécharger la réservation ID: " + reservationId);
                session.setAttribute("error", "Vous n'êtes pas autorisé à télécharger ce billet.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            LOGGER.info("Génération du PDF pour la réservation ID: " + reservationId);
            generatePDF(reservation, response);
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de réservation invalide.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la génération du PDF: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la génération du PDF: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/monCompte");
        }
    }

    private void generatePDF(Reservation reservation, HttpServletResponse response) throws IOException {
        // Définir le type de contenu et l'en-tête pour le téléchargement
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=billet_" + reservation.getId() + ".pdf");

        // Ajouter un log pour confirmer que la méthode est appelée
        LOGGER.info("Début de la génération du PDF pour la réservation ID: " + reservation.getId());

        // Créer un document PDF avec PDFBox
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
                contentStream.showText("ID Réservation : " + reservation.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(50, 680);
                contentStream.showText("Utilisateur : " + reservation.getUser().getEmail());
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 650);
                contentStream.showText("Détails du voyage :");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                float yPosition = 620;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Gare de départ : " + reservation.getVoyage().getTrajet().getDepartStation().getName() + " (" + reservation.getVoyage().getTrajet().getDepartStation().getCity() + ")");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Gare d'arrivée : " + reservation.getVoyage().getTrajet().getArrivalStation().getName() + " (" + reservation.getVoyage().getTrajet().getArrivalStation().getCity() + ")");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Heure de départ : " + reservation.getVoyage().getHeureDepartFormatted());
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Heure d'arrivée : " + reservation.getVoyage().getHeureArriveeFormatted());
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Durée : " + reservation.getVoyage().getDuree());
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Prix : " + reservation.getVoyage().getPrix() + " €");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Classe : " + reservation.getClasse());
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Préférences : " + (reservation.getPreferences() != null ? reservation.getPreferences() : "Aucune"));
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("- Date de réservation : " + reservation.getDateReservation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                contentStream.endText();
                yPosition -= 20;

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("État : " + reservation.getEtat());
                contentStream.endText();
                yPosition -= 20;

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Date : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                contentStream.endText();
            }

            // Écrire le document dans la réponse
            LOGGER.info("Écriture du PDF dans la réponse pour la réservation ID: " + reservation.getId());
            response.getOutputStream().flush(); // S'assurer que le flux est vidé avant d'écrire
            document.save(response.getOutputStream());
            response.getOutputStream().flush(); // S'assurer que tout est envoyé au client
            LOGGER.info("PDF généré et envoyé avec succès pour la réservation ID: " + reservation.getId());
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'écriture du PDF: " + e.getMessage());
            throw new IOException("Erreur lors de l'écriture du PDF", e);
        }
    }
}