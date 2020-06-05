//Elaborado por CISM <3 

package org.fbcmd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fbcmd4j.utils.Utils;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class Main 
{

	static final Logger logger = LogManager.getLogger(Main.class);
	private static final String CONFIG_DIR = "config";
	private static final String CONFIG_FILE = "fbcmd4j.properties";

	public static void main(String[] args) {
		logger.info("Iniciando app CISM");
		Facebook fb = null;
		Properties props = null;

		try {
			props = Utils.loadConfigFile(CONFIG_DIR, CONFIG_FILE);
		} catch (IOException ex) {
			logger.error(ex);
		}

		int option = 1;
		try {
			Scanner scan = new Scanner(System.in);
			while (true) {
				fb = Utils.configFacebook(props);
				System.out.println("Opciones: \n" +
						"(0) Configuración de Cliente \n" + 
						"(1) Revisar NewsFeed \n" + 
						"(2) Revisar Wall \n" + 
						"(3) ¿Qué estás pensando? \n" +
						"(4) Publicar Link \n" + 
						"(5) Salir \n" + 
						"\nSelecciona una opción:");
				try {
					option = scan.nextInt();
					scan.nextLine();
					switch (option) {
					case 0:
						Utils.configTokens(CONFIG_DIR, CONFIG_FILE, props, scan);
						props = Utils.loadConfigFile(CONFIG_DIR, CONFIG_FILE);
						break;//Elaborado por CISM <3 
					case 1:
						System.out.println("NewsFeed:");
						logger.info("Acceso a NewsFeed");
						ResponseList<Post> newsFeed = fb.getFeed();
						for (Post p : newsFeed) {
							Utils.desplegarPost(p);
						}
						preguntarGuardar("NewsFeed", newsFeed, scan);
						break;//Elaborado por CISM <3 
					case 2:
						System.out.println("Wall:");
						logger.info("Acceso a opción Wall");
						ResponseList<Post> wall = fb.getPosts();
						for (Post p : wall) {
							Utils.desplegarPost(p);
						}
						preguntarGuardar("Wall", wall, scan);
						break;//Elaborado por CISM <3 
					case 3:
						System.out.println("¿Qué estás pensando?");
						logger.info("Acceso a estatus");
						String estado = scan.nextLine();
						Utils.publicarStatus(estado, fb);
						break;
					case 4://Elaborado por CISM <3 
						System.out.println("Insert link: ");
						logger.info("Posteo de link en Wall");
						String link = scan.nextLine();
						Utils.publicarLink(link, fb);
						break;
					case 5:
						System.out.println("Finalizando...");
						logger.info("Fin de la app CISM");
						System.exit(0);
						break;
					default:
						break;//Elaborado por CISM <3 
					}
				} catch (InputMismatchException ex) {
					System.out.println("Revisar Log para conocer el error");
					logger.error("Opción inválida. %s. \n", ex.getClass());
				} catch (FacebookException ex) {
					System.out.println("Revisar Log para conocer el error");
					logger.error(ex.getErrorMessage());
				} catch (Exception ex) {
					System.out.println("Revisar Log para conocer el error");
					logger.error(ex);
				}
				System.out.println();
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}//Elaborado por CISM <3 

	public static void preguntarGuardar(String fileName, ResponseList<Post> posts, Scanner scan) {
		System.out.println("¿Desea guardar los resultados? S/N ");
		String option = scan.nextLine();

		if (option.contains("s") || option.contains("S")) {
			List<Post> ps = new ArrayList<>();
			int n = 0;

			while (n <= 0) {//Elaborado por CISM <3 
				try {
					System.out.println("¿Cuántos posts deseas guardar?");
					n = Integer.parseInt(scan.nextLine());

					if (n <= 0) {
						System.out.println("Ingresa un número válido");
					} else {//Elaborado por CISM <3 
						for (int i = 0; i < n; i++) {
							if (i > posts.size() - 1)
								break;
							ps.add(posts.get(i));
						}
					}
				} catch (NumberFormatException e) {
					logger.error(e);
				}//Elaborado por CISM <3 
			}

			Utils.guardatPostArchivo(fileName, ps);
		}
	}
	
}