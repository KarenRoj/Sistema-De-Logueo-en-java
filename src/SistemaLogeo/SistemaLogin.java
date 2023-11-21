package SistemaLogeo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SistemaLogin {

	private static Map<String, String> usuarios = new HashMap<>();
	private static Map<String, Integer> intentosFallidos = new HashMap<>();

	public static void main(String[] args) {
		// Usuarios preregistrados
		registrarUsuario("usuario1", "password1");
		registrarUsuario("usuario2", "password2");
		// Agrega más usuarios preregistrados si es necesario

		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("1. Login");
			System.out.println("2. Registro");
			System.out.println("3. Salir");
			System.out.print("Seleccione una opción: ");
			int opcion = scanner.nextInt();
			scanner.nextLine(); // Consume la nueva línea

			switch (opcion) {
			case 1:
				login(scanner);
				break;
			case 2:
				registro(scanner);
				break;
			case 3:
				System.out.println("Saliendo del programa. ¡Hasta luego!");
				System.exit(0);
				break;
			default:
				System.out.println("Opción no válida. Intente nuevamente.");
			}
		}
	}

	private static void login(Scanner scanner) {
		System.out.print("Ingrese el usuario o correo electrónico: ");
		String usuario = scanner.nextLine();
		System.out.print("Ingrese la contraseña: ");
		String contraseña = scanner.nextLine();

		if (verificarCredenciales(usuario, contraseña)) {
			System.out.println("Acceso concedido.");
		} else {
			int intentos = intentosFallidos.getOrDefault(usuario, 0) + 1;
			intentosFallidos.put(usuario, intentos);

			if (intentos >= 3) {
				System.out.println("Usuario bloqueado. Intente con otro usuario.");
			} else {
				System.out.println("Acceso denegado. Intento fallido número " + intentos);
			}
		}
	}

	private static boolean verificarCredenciales(String usuario, String contraseña) {
		String contraseñaEncriptada = encriptarContraseña(contraseña);
		return usuarios.containsKey(usuario) && usuarios.get(usuario).equals(contraseñaEncriptada);
	}

	private static void registro(Scanner scanner) {
		System.out.print("Ingrese el nuevo usuario: ");
		String nuevoUsuario = scanner.nextLine();

		if (usuarios.containsKey(nuevoUsuario)) {
			System.out.println("El usuario ya existe. Intente con otro usuario.");
		} else {
			System.out.print("Ingrese la contraseña para el nuevo usuario: ");
			String nuevaContraseña = scanner.nextLine();
			registrarUsuario(nuevoUsuario, nuevaContraseña);
			System.out.println("Usuario nuevo registrado.");
		}
	}

	private static void registrarUsuario(String usuario, String contraseña) {
		String contraseñaEncriptada = encriptarContraseña(contraseña);
		usuarios.put(usuario, contraseñaEncriptada);
	}

	private static String encriptarContraseña(String contraseña) {
	    if (contraseña == null || contraseña.isEmpty()) {
	        throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
	    }
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hashBytes = md.digest(contraseña.getBytes());
	        StringBuilder hashString = new StringBuilder();

	        for (byte b : hashBytes) {
	            hashString.append(String.format("%02x", b));
	        }

	        return hashString.toString();
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Error al encriptar la contraseña", e);
	    }
	}
}