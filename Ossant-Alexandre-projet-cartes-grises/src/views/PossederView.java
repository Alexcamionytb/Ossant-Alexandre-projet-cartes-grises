package views;

import controllers.PossederController;
import models.Posseder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PossederView extends JFrame {

    private JTable table;  // Table pour afficher les données
    private DefaultTableModel tableModel;  // Modèle de table pour la gestion des données dans la JTable
    private PossederController possederController = new PossederController();  // Instance du contrôleur pour gérer les opérations sur les données
    private JButton addButton, updateButton, deleteButton, backButton;  // Boutons pour ajouter, modifier, supprimer et revenir

    public PossederView() {
        // Paramètres de la fenêtre principale
        setTitle("Affichage des données POSSEDER");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation du modèle de la table avec les noms de colonnes
        tableModel = new DefaultTableModel(new String[]{"Nom Propriétaire", "Modèle Véhicule", "Date Début", "Date Fin"}, 0);
        table = new JTable(tableModel);  // Création de la JTable avec le modèle
        add(new JScrollPane(table), BorderLayout.CENTER);  // Ajout de la table dans la fenêtre avec un JScrollPane pour la défilement

        // Méthode pour rafraîchir les données de la table
        refreshTable();

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Ajouter");
        updateButton = new JButton("Modifier");
        deleteButton = new JButton("Supprimer");
        backButton = new JButton("Retour en arrière");

        // Ajouter les boutons dans le panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Ajouter le panel au bas de la fenêtre
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter des écouteurs d'action pour chaque bouton

        // Action du bouton "Ajouter"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Création des champs de saisie pour le formulaire
                JTextField proprietaireField = new JTextField();
                JTextField modeleField = new JTextField();
                JTextField dateDebutField = new JTextField();
                JTextField dateFinField = new JTextField();

                // Création du panneau avec un GridLayout pour l'agencement des champs
                JPanel panel = new JPanel(new GridLayout(4, 2));
                panel.add(new JLabel("Nom Propriétaire:"));
                panel.add(proprietaireField);
                panel.add(new JLabel("Modèle Véhicule:"));
                panel.add(modeleField);
                panel.add(new JLabel("Date Début (yyyy-MM-dd):"));
                panel.add(dateDebutField);
                panel.add(new JLabel("Date Fin (yyyy-MM-dd, optionnel):"));
                panel.add(dateFinField);

                // Afficher une boîte de dialogue pour la saisie des données
                int result = JOptionPane.showConfirmDialog(null, panel, "Ajouter une propriété", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        // Récupération des données saisies
                        String nomProprietaire = proprietaireField.getText();
                        String nomModele = modeleField.getText();
                        Date dateDebut = new SimpleDateFormat("yyyy-MM-dd").parse(dateDebutField.getText());
                        Date dateFin = dateFinField.getText().isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd").parse(dateFinField.getText());

                        // Récupération des IDs à partir des noms
                        int idProprietaire = possederController.getIdProprietaire(nomProprietaire);
                        int idVehicule = possederController.getIdVehiculeParModele(nomModele);

                        // Ajout d'un nouveau "posséder" dans la base de données
                        possederController.addPosseder(new Posseder(idProprietaire, idVehicule, dateDebut, dateFin));
                        refreshTable();  // Rafraîchissement de la table après ajout
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout.");
                    }
                }
            }
        });

        // Action du bouton "Modifier"
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {  // Vérifier si une ligne est sélectionnée
                    try {
                        // Récupérer les valeurs existantes dans la ligne sélectionnée
                        String originalNomProprietaire = table.getValueAt(selectedRow, 0).toString();
                        String originalNomModele = table.getValueAt(selectedRow, 1).toString();
                        int originalIdProprietaire = possederController.getIdProprietaire(originalNomProprietaire);
                        int originalIdVehicule = possederController.getIdVehiculeParModele(originalNomModele);

                        // Demander à l'utilisateur de saisir de nouvelles valeurs
                        String nomProprietaire = JOptionPane.showInputDialog("Nouveau nom propriétaire:", originalNomProprietaire);
                        String nomModele = JOptionPane.showInputDialog("Nouveau modèle véhicule:", originalNomModele);
                        String dateDebutStr = JOptionPane.showInputDialog("Nouvelle date début (yyyy-MM-dd):", table.getValueAt(selectedRow, 2).toString());
                        String dateFinStr = JOptionPane.showInputDialog("Nouvelle date fin (yyyy-MM-dd):", table.getValueAt(selectedRow, 3).toString());

                        Date dateDebut = new SimpleDateFormat("yyyy-MM-dd").parse(dateDebutStr);
                        Date dateFin = dateFinStr.equals("N/A") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(dateFinStr);

                        // Récupération des nouveaux IDs
                        int idProprietaire = possederController.getIdProprietaire(nomProprietaire);
                        int idVehicule = possederController.getIdVehiculeParModele(nomModele);

                        // Supprimer l'ancien enregistrement et ajouter le nouveau
                        possederController.deletePosseder(originalIdProprietaire, originalIdVehicule);
                        possederController.addPosseder(new Posseder(idProprietaire, idVehicule, dateDebut, dateFin));
                        refreshTable();  // Rafraîchissement de la table après modification
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur lors de la modification.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à modifier.");
                }
            }
        });

        // Action du bouton "Retour en arrière"
        backButton.addActionListener(e -> dispose());  // Fermer la fenêtre actuelle

        // Action du bouton "Supprimer"
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {  // Vérifier si une ligne est sélectionnée
                    String nomProprietaire = table.getValueAt(selectedRow, 0).toString();
                    String nomModele = table.getValueAt(selectedRow, 1).toString();

                    // Récupérer les IDs à partir des noms
                    int idProprietaire = possederController.getIdProprietaire(nomProprietaire);
                    int idVehicule = possederController.getIdVehiculeParModele(nomModele);

                    // Supprimer l'enregistrement de la base de données
                    possederController.deletePosseder(idProprietaire, idVehicule);
                    refreshTable();  // Rafraîchissement de la table après suppression
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à supprimer.");
                }
            }
        });

        setVisible(true);  // Afficher la fenêtre
    }

    // Méthode pour rafraîchir les données de la table en fonction des données de la base
    private void refreshTable() {
        List<Posseder> possederList = possederController.getAllPosseder();
        tableModel.setRowCount(0);  // Vider la table avant de la remplir avec de nouvelles données
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Posseder p : possederList) {
            // Ajouter une ligne pour chaque élément de la liste
            tableModel.addRow(new Object[]{
                possederController.getNomProprietaire(p.getIdProprietaire()),  // Nom du propriétaire
                possederController.getNomModele(p.getIdVehicule()),  // Modèle du véhicule
                sdf.format(p.getDateDebutPropriete()),  // Format de la date de début
                p.getDateFinPropriete() != null ? sdf.format(p.getDateFinPropriete()) : "N/A"  // Format de la date de fin, ou "N/A" si null
            });
        }
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        new PossederView();
    }
}
