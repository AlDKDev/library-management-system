package pe.edu.elitec.library_management_system.exception;

public class InvalidCategoryException extends RuntimeException{
    private final String category;

    public InvalidCategoryException(String category) {
        super("La categoría '" + category + "' no es válida. Categorías permitidas: FICCION, NO_FICCION, CIENCIA, TECNOLOGIA, HISTORIA, ARTE, FILOSOFIA, LITERATURA, EDUCACION, REFERENCIA");
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
