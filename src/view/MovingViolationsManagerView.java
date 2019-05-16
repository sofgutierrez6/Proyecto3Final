package view;

public class MovingViolationsManagerView {

	public void printMenu() {
		System.out.println("---------ISIS 1206 - Estructuras de datos----------");
		System.out.println("---------------------Proyecto 3----------------------");
		System.out.println("0. Cargar el Grafo No Dirigido de la malla vial: Downtown o Ciudad Completa");
		
		System.out.println("1. Encontrar el camino de costo mínimo para un viaje entre  dos vertices.. (REQ 1A)");
		System.out.println("2. Determinar los n vértices con mayor número de infracciones y sus componentes conectadas (REQ 2A)");
		
		System.out.println("3. Encontrar el camino más corto para un viaje entre  dos vertices. (REQ 1B)");		
		System.out.println("4. Definir una cuadricula regular de N columnas por M filas.  (REQ 2B)"); 
		
		System.out.println("5. Calcular un árbol de expansión mínima (MST) con criterio distancia, utilizando el algoritmo de Kruskal (REQ 1C)");
		System.out.println("6. Calcular un árbol de expansión mínima (MST) con criterio distancia, utilizando el algoritmo de Prim. (REQ 2C)");
		System.out.println("7. Calcular los caminos de costo mínimo con criterio distancia que conecten los vértices resultado "
				+ "de la aproximación de las ubicaciones de la cuadricula N x M encontrados en el punto 5. (REQ 3C)");
		System.out.println("8. Encontrar el camino más corto para un viaje entre dos vertices. (REQ 4C)");
		
		System.out.println("9. Salir");
		System.out.println("Digite el numero de opcion para ejecutar la tarea, luego presione enter: (Ej., 1):");
		
	}

	public void printMessage(String string) {
		System.out.println(string);
		
	}
	
}