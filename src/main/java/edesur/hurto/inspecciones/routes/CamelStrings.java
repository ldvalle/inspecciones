package edesur.hurto.inspecciones.routes;

public final class CamelStrings {
    private CamelStrings() {}

    public static String simpleHeader(String heanerName) {
        return "${header." + heanerName + "}";
    }

    public static String property(String property) {
        return "${property." + property + "}";
    }

    public static String stringWithHeaders(String fmt, String... headers) {
        String[] parametros = new String[headers.length];
        for (int i = 0; i < headers.length; i++) {
            parametros[i] = "${header." + headers[i] + "}";
        }
        return String.format(fmt, (Object[]) parametros);
    }
}
