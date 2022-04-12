/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Entity.Employe;
import Entity.Rayon;
import Entity.Secteur;
import Entity.Travailler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author jbuffeteau
 */
public class FonctionsMetier
{
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection cnx;

    public FonctionsMetier()
    {
        cnx = ConnexionBDD.getCnx();
    }
    
    public ArrayList<Secteur> GetAllSecteurs()
    {
        ArrayList<Secteur> desSecteur = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("select numS,nomS from secteur");
            rs = ps.executeQuery();
            
            while(rs.next())
            {
                Secteur s = new Secteur(rs.getInt("numS"), rs.getString("nomS"));
                desSecteur.add(s);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return desSecteur;
    }
    
    public ArrayList<Employe> GetAllEmployes()
    {
        ArrayList<Employe> desEmployes = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("select numE,prenomE from employe");
            rs = ps.executeQuery();
            
            while(rs.next())
            {
                Employe e = new Employe(rs.getInt("numE"), rs.getString("prenomE"));
                desEmployes.add(e);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return desEmployes;
    }
    
    public ArrayList<Rayon> GetAllRayonsByIdsecteur(int numSecteur)
    {
        ArrayList<Rayon> desRayons = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("select numR,nomR from rayon where numsecteur="+numSecteur);
            rs = ps.executeQuery();
            
            while(rs.next())
            {
                Rayon r = new Rayon(rs.getInt("numR"),rs.getString("nomR") );
                desRayons.add(r);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return desRayons;
    }
    
    public ArrayList<Travailler> GetAllTravailler(int numRayon)
    {
        ArrayList<Travailler> desTravails = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("select codeE,date,temps from travailler where codeR="+numRayon);
            rs = ps.executeQuery();
            
            
            
            while(rs.next())
            {
                ps = cnx.prepareStatement("select numE,prenomE from employe  where numE="+rs.getInt(1));
                ResultSet rs2 = ps.executeQuery();
                rs2.next();
                Employe e = new Employe(rs2.getInt("numE"), rs2.getString("prenomE"));
                
                
                Travailler t = new Travailler(e, rs.getString(2),rs.getInt(3));
                desTravails.add(t); 
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return desTravails;
    }
    
    public int GetIdEmployeByNom(String nomEmploye)
    {
        int numE=0;
        try {
            ps = cnx.prepareStatement("select numE from employe where prenomE="+nomEmploye);
            rs = ps.executeQuery();
            rs.next();
            numE=rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numE;
    }
    
    public int TotalHeuresRayon(int numRayon)
    {
        int totHeure=0;
        try {
            
            ps = cnx.prepareStatement("select sum(temps) from travailler where codeR="+numRayon);
            rs = ps.executeQuery();
            rs.next();
            totHeure=rs.getInt(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totHeure;
    }
    
    public void ModifierTemps(int codeEmploye, int CodeRayon, String uneDate,int nouveauTemps)
    {
        try {
            
            ps = cnx.prepareStatement("UPDATE travailler SET temps="+nouveauTemps+" WHERE codeE="+codeEmploye+" and codeR="+CodeRayon+" and date='"+uneDate+"'");
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void InsererTemps(int codeEmploye, int CodeRayon,int nouveauTemps)
    {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate localDate = LocalDate.now();
            dtf.format(localDate);
            
            ps = cnx.prepareStatement("INSERT INTO `travailler`(`codeE`, `codeR`, `date`, `temps`) VALUES ("+codeEmploye+","+CodeRayon+",'"+dtf+"',"+nouveauTemps+")");
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
