			/*
			 * zähler für spell slots und fähigkeiten (,,leicht'' erweiterbar)
			 * verschiedene Profile für verschiedene Chars (erweiterbar)
			 * 1.Frage Profil (new or old)
			 * if new frage neues Profil anlegen
			 * if old bestehendes Profil auswählen(0=doch ein neues, 1=Char1, ...)
			 * 2 tabellen 
			 */

			/*
			 * gespeicherte Profile wieder öfnnen 
			 * richtige anzahl spell slots anzeigen
			 */

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SlotCounter {

	static final String[][] spaltenName = 	{	{"1st Level", "2nd Level","3rd Level", "4th Level", "5th Level", "6th Level", "7th Level", "8th Level", "9th Level"},
												{"1st Level", "2nd Level","3rd Level", "4th Level", "5th Level"},
												{"1st Level", "2nd Level","3rd Level", "4th Level", "5th Level","6th Level Mystic Arcanum","7th Level Mystic Arcanum","8th Level Mystic Arcanum","9th Level Mystic Arcanum"} 
											}; 
	static String name;
	static int level = -1;
	static int castTyp;
	static String[] castTypes = {"Fullcaster","Halfcaster","Warlock"};
	static String[][] casterTable;
	
	public static void main(String[] args) {
		if (profilAbfragen()) {									//wenn profilAbfragen() funktioniert(mit true zurück kommt) dann füre showTableSpell() aus
			showTableSpells();
		}
		
		}
	
	static boolean profilAbfragen() {
		int profil = JOptionPane.showConfirmDialog(null, "Neues Profil anlegen??", "Welches Profil?", 0, 3);		//in klammer sind parameter der fenster
		if (profil == 0) {
			name =	JOptionPane.showInputDialog(null, "Name", "Neues Profil anlegen",3);
			while (level < 0 || level > 20){
				try {
					level = Integer.parseInt(JOptionPane.showInputDialog(null, "Welches Level ist der Charakter?", "Neues Profil anlegen", 3));		//parse wandelt die Eingabe in einen int wert um (ganze Zahl)
					if (level < 0 || level > 20) {
						throw new NumberFormatException();						//wenn level nicht zwischen 1 und 20 dann wirf(throw) neue NumberFormatException mit den werten null;"Es muss eine Zahl zwischen 1 und 20 sein", "Fehler", 0);
					}
				}
				catch(NumberFormatException e){								// e standard falls ein fehler kommen soll      //catch wegen try
					JOptionPane.showMessageDialog(null, "Es muss eine Zahl zwischen 1 und 20 sein", "Fehler", 0);	
				}
			}
			castTyp = JOptionPane.showOptionDialog(null, "Was für ein Caster Typ ist der Charakter?", "Neues Profil anlegen", 1, 3, null, castTypes, null);
			if (castTyp < 0) {
				return false;											//wenn programm geschlossen wird soll ein false(für das boolean in profilAbfrage()) zurückkommen und er soll returnen
			}
			casterTable = new String[4][spaltenName[castTyp].length];			//erstellt neuen casterTable mit der größe [4 (für maximale spell slots)][Anzahl spell Level je nach casterTypes]
			
		}
		else if (profil == 1) {
			String [] names = profilOptionsAbfragen();																	// profilOptionsAbfragen gibt ein Array (Etoirir,Barde,...) zurück und speichert ihn als String Array in der Variable names 
			int auswahl = JOptionPane.showOptionDialog(null, "Wähle ein Profil aus", "Profil auswählen", 1, 3, null, names, null);
			name = names [auswahl];																										//in name wird auswahl(0. eintrag z.b.) von names gespeichert
			try(FileInputStream writer = new FileInputStream(name + ".profile");		
					ObjectInputStream ding = new ObjectInputStream(writer)){					
					castTyp = (int) ding.readObject();													// konvertiert es in ein int (0,1,2,...)
					level = (int) ding.readObject();
					casterTable = (String[][]) ding.readObject();
				
	
				} catch (IOException | ClassNotFoundException e1) {											
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		else return false;	
		return true;   													//wenn das neue Profil ertellt wurde(profilAbfrage()) kommt ein true für das boolean von profilAbfragen()heraus, danach führt er showTableSpells() aus
		
	}
	static String [] profilOptionsAbfragen() {
		File file = new File ("./");									// erstellt neues Objekt (typ File) welches auf den Order verweist in welchem es (SlotCounter.java) gerade ausgeführt wird ("./")
		ArrayList<String> nameLists = new ArrayList<String>();			// ertsellen neues Objekt vom typ Arraylist (vergrößerbares array) in welcher die namen gespeichert werden sollen (String)
		for(File f : file.listFiles()) {								// für jede File die wir haben(test,testen,src,...) soll er etwas machen
			if (f.getName().endsWith(".profile")) {						// wenn die File mit .profile ended dann...
				nameLists.add(f.getName().replace(".profile", ""));	 	// tragen wir in die liste den Namen des Charakters ein (tausche .profile aus mit "  ")		
			}
		}
	return nameLists.toArray(new String [0]);							// alle gespeicherten Profile (Etoirir, Barde,...) wird zurückgegeben 
	}
 	
	static void showTableSpells() {
		
		
	
		JTable table = new JTable(casterTable, spaltenName[castTyp]);	//erstellt new tabelle (objekt) mit den werten(tabelle zum speichern der spells, kopf der tabelle[fullcaster or halfcaster or warlock])
		JScrollPane scrollpane = new JScrollPane(table);    			//standard ist scrollen aus deswegen MUSS!!!!(laut Fabi) man es anschalten weil darum
	    table.setFillsViewportHeight(true);					  			//lässt tabelle gesamte scrollpane ausfüllen
	    
	    JFrame tableFenster = new JFrame("Spell Slot Counter");			//JFrame ist basis für ein Fenster
	    tableFenster.add(scrollpane);									//fügt scrollpane hinzu als componente
	    tableFenster.setSize(500, 300);
	    tableFenster.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);	//schließt das Fenster aber speichert (addWindowsListener()) vor dem beenden
	    tableFenster.setVisible(true);
	    tableFenster.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				String dateiname = name + ".profile";
				File file = new File (dateiname);						//erstellt neues Objekt vom typ File
				if (!file.exists()) {
					try {												//versucht new file zu createn ansonsten kommt ein fehler (try/catch)
						file.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();							//gibt in der console einen Fehler aus (die roten Exceptions)
					}
				}
				try(FileOutputStream writer = new FileOutputStream(file,false);		//ganz viel Voodoo kram den keiner versteht(frag fabi lieber nicht)öffnet die file und  macht dass die File überschrieben wird (false)
					ObjectOutputStream ding = new ObjectOutputStream(writer)){		//speichert sachen im writer (file)
					ding.writeObject(castTyp);										//nimmt den wert der in castTyp steht (fullcaster, halfcaster, warlock) und schreibt ihn in die datei(file)
					ding.writeObject(level);										//nimmt den wert der in level steht (1-20) und schreibt ihn in die datei(file)
					ding.writeObject(casterTable);									//nimmt den wert der in casterTable steht (die eingetragenen Spells) und schreibt ihn in die datei(file)
																					//weil try wird automatisch geschlossen
				
				
				} catch (IOException e1) {											
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0); 													//beendet das programm nach dem speichern
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    
	}
}
















