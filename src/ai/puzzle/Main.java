package ai.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Main {

	public enum Reglas { UP, LEFT, DOWN, RIGHT }

	public static String obtenerClaveDeEstado(Integer[][] estado) {
		if(estado == null) {
			return null;
		}

		String clave = "";

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				clave += estado[i][j];
			}
		}

		return clave;
	}

	public static boolean Cond_Term(Integer[][] estadoActual, Integer[][] estadoFinal) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (estadoActual[i][j] != estadoFinal[i][j]) {
					return false;
				}
			}
		}

		return true;
	}

	public static ArrayList<String> ConstruirCamino(HashMap<String, Estado> AB, Estado Est_Act) {
		ArrayList<String> cam = new ArrayList<>();

		String actual = obtenerClaveDeEstado(Est_Act.getEstado());

		while (actual != null) {
			cam.add(actual);
			actual = AB.get(actual).getPadre();
		}

		return cam;
	}

	public static String mostrarEstado (String estado) {
		String resp = "";
		int index = 0;

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				resp += new Integer("" + estado.charAt(index++)) + " ";
			}
			resp += "\n";
		}

		return resp;
	}

	public static boolean es_movimiento_valido(Integer[][] estado, Reglas regla) {
		int empty_box_r, empty_box_c;
		empty_box_r = empty_box_c = 0;
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (estado[i][j] == 0) {
					empty_box_r = i;
					empty_box_c = j;
					i = j = 3;
				}
			}
		}
		
		switch (regla) {
		case UP:
			--empty_box_r;
			if (empty_box_r == -1) {
				return false;
			}
		break;
		case LEFT:
			--empty_box_c;
			if (empty_box_c == -1) {
				return false;
			}
		break;
		case DOWN:
			++empty_box_r;
			if (empty_box_r == 3) {
				return false;
			}
		break;
		case RIGHT:
			++empty_box_c;
			if (empty_box_c == 3) {
				return false;
			}
		break;
		default:
		break;
		}
		
		return true;
	}

	public static LinkedList<Reglas> Reglas_aplicables(Integer[][] Est_Act) {
		LinkedList<Reglas> RA = new LinkedList<>();
		
		for (Reglas regla : Reglas.values()) {
			if (es_movimiento_valido(Est_Act, regla)) {
				RA.add(regla);
			}
		}
		
		return RA;
	}

	public static Estado Aplicar(Reglas R, Integer[][] Est_Act) {
		Integer[][] copia = {	{ Est_Act[0][0], Est_Act[0][1], Est_Act[0][2] },
								{ Est_Act[1][0], Est_Act[1][1], Est_Act[1][2] },
								{ Est_Act[2][0], Est_Act[2][1], Est_Act[2][2] }
							};
		
		int empty_box_r, empty_box_c;
		empty_box_r = empty_box_c = 0;
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (copia[i][j] == 0) {
					empty_box_r = i;
					empty_box_c = j;
					i = j = 3;
				}
			}
		}
		
		switch (R) {
		case UP:
			--empty_box_r;
			copia[empty_box_r + 1][empty_box_c] = copia[empty_box_r][empty_box_c];
			copia[empty_box_r][empty_box_c] = 0;
		break;
		case LEFT:
			--empty_box_c;
			copia[empty_box_r][empty_box_c + 1] = copia[empty_box_r][empty_box_c];
			copia[empty_box_r][empty_box_c] = 0;
		break;
		case DOWN:
			++empty_box_r;
			copia[empty_box_r - 1][empty_box_c] = copia[empty_box_r][empty_box_c];
			copia[empty_box_r][empty_box_c] = 0;
		break;
		case RIGHT:
			++empty_box_c;
			copia[empty_box_r][empty_box_c - 1] = copia[empty_box_r][empty_box_c];
			copia[empty_box_r][empty_box_c] = 0;
		break;
		default:
		break;
		}
		
		Estado Est_Gen = new Estado();
		
		Est_Gen.setEstado(copia);
		
		return Est_Gen;
	}

	public static boolean noEsAncestroDeEnAB(Estado Est_Gen, Estado Est_Act, HashMap<String, Estado> AB) {
		String actual = obtenerClaveDeEstado(Est_Act.getEstado());
		String est_gen = obtenerClaveDeEstado(Est_Gen.getEstado());
		
		while (actual != null) {
			if (actual.equals(est_gen)) {
				return false;
			}
			actual = AB.get(actual).getPadre();
		}
		
		return true;
	}

	public static double Costo(Estado Est_Act, Estado Est_Gen) {
		return 1.0;
	}

	public static double calcular_h(Integer[][] estado, Integer[][] estado_final) {
		double h = 0.0;
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (estado[i][j] != estado_final[i][j]) {
					h += 1.0;
				}
			}
		}
		
		return h;
	}

	public static void Actualizar(PriorityQueue<Estado> ABIERTOS, Estado Est_Gen) {
		ABIERTOS.remove(Est_Gen);
		ABIERTOS.add(Est_Gen);
	}

	public static Estado buscarEnAbiertosYCerrados(Estado Est_Gen, PriorityQueue<Estado> ABIERTOS, PriorityQueue<Estado> CERRADOS) {
		for (Estado e: ABIERTOS) {
			if (e.equals(Est_Gen)) {
				return e;
			}
		}

		for (Estado e: CERRADOS) {
			if (e.equals(Est_Gen)) {
				return e;
			}
		}

		return null;
	}

	public static ArrayList<String> a_estrella(Integer[][] estadoInicial, Integer[][] estadoFinal) {
		Estado E_Ini = new Estado(estadoInicial);
		HashMap<String, Estado> AB = new HashMap<>();
		PriorityQueue<Estado> ABIERTOS = new PriorityQueue<>();
		PriorityQueue<Estado> CERRADOS = new PriorityQueue<>();
		boolean EX = false;
		ArrayList<String> cam = new ArrayList<>();

		int cont = 0;

		E_Ini.setPadre(null);
		AB.put(obtenerClaveDeEstado(E_Ini.getEstado()), E_Ini);
		ABIERTOS.add(E_Ini);
		
		long startTime = System.nanoTime();

		while (!ABIERTOS.isEmpty() && (EX == false)) {
			++cont;
			Estado Est_Act = ABIERTOS.remove();
			CERRADOS.add(Est_Act);
			if (Cond_Term(Est_Act.getEstado(), estadoFinal)) {
				cam = ConstruirCamino(AB, Est_Act);
				EX = true;
			} else {
				LinkedList<Reglas> RA = Reglas_aplicables(Est_Act.getEstado());
				while (!RA.isEmpty()) {
					Reglas R = RA.pop();
					Estado Est_Gen = Aplicar(R, Est_Act.getEstado());
					if (noEsAncestroDeEnAB(Est_Gen, Est_Act, AB)) {
						double Costo_Temp = Est_Act.getG() + Costo(Est_Act, Est_Gen);
						if (!ABIERTOS.contains(Est_Gen) && !CERRADOS.contains(Est_Gen)) {
							Est_Gen.setG(Costo_Temp);
							Est_Gen.setH(calcular_h(Est_Gen.getEstado(), estadoFinal));
							Est_Gen.setF(Est_Gen.getG() + Est_Gen.getH());
							Est_Gen.setPadre(obtenerClaveDeEstado(Est_Act.getEstado()));
							ABIERTOS.add(Est_Gen);
							AB.put(obtenerClaveDeEstado(Est_Gen.getEstado()), Est_Gen);
						} else {
							Est_Gen = buscarEnAbiertosYCerrados(Est_Gen, ABIERTOS, CERRADOS);
							if (Costo_Temp < Est_Gen.getG()) {
								Est_Gen.setG(Costo_Temp);
								Est_Gen.setH(calcular_h(Est_Gen.getEstado(), estadoFinal));
								Est_Gen.setF(Est_Gen.getG() + Est_Gen.getH());
								Est_Gen.setPadre(obtenerClaveDeEstado(Est_Act.getEstado()));
								AB.put(obtenerClaveDeEstado(Est_Gen.getEstado()), Est_Gen);
								if (ABIERTOS.contains(Est_Gen)) {
									Actualizar(ABIERTOS, Est_Gen);
								}
								if (CERRADOS.contains(Est_Gen)) {
									CERRADOS.remove(Est_Gen);
									ABIERTOS.add(Est_Gen);
								}
							}
						}
					}
				}
			}
		}
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;

		System.out.println("Contador: " + cont);
		System.out.println("Estados explorados: " + (ABIERTOS.size() + CERRADOS.size()));
		System.out.println("Duracion: " + (duration / 1000000) + " milisegundos");

		if (EX) {
			System.out.println("SI");
		} else {
			System.out.println("NO");
		}

		return cam;
	}

	public static void main(String[] args) {
		Integer[][] estadoInicial = { 	{ 7, 3, 5 },
										{ 4, 2, 0 },
										{ 1, 6, 8 }
									};
		
		Integer[][] estadoFinal = 	{ 	{ 1, 2, 3 },
										{ 4, 5, 6 },
										{ 7, 8, 0 }
									};

		ArrayList<String> cam = a_estrella(estadoInicial, estadoFinal);

		System.out.println(cam.size());

		for (String estado : cam) {
			System.out.println("-----");
			System.out.println(mostrarEstado(estado));
		}
	}
}
