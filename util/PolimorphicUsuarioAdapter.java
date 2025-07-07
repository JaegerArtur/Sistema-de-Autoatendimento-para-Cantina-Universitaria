package util;

import com.google.gson.*;
import model.*;
import java.lang.reflect.Type;
import java.util.*;

public class PolimorphicUsuarioAdapter implements JsonDeserializer<Usuario>, JsonSerializer<Usuario> {
    /**
     * Serializa um objeto Usuario (Membro, Admin, Visitante) para JSON, incluindo o campo "tipo".
     */
    @Override
    public JsonElement serialize(Usuario src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = (JsonObject) context.serialize(src, src.getClass());
        String tipo;
        if (src instanceof Membro) tipo = "MEMBRO";
        else if (src instanceof Admin) tipo = "ADMIN";
        else if (src instanceof Visitante) tipo = "VISITANTE";
        else tipo = "";
        obj.addProperty("tipo", tipo);
        return obj;
    }
    /**
     * Deserializa um objeto JSON em uma instância de Usuario, determinando o tipo específico
     * (Membro, Admin ou Visitante) com base no campo "tipo" do JSON.
     *
     * @param json O elemento JSON a ser deserializado.
     * @param typeOfT O tipo do objeto a ser retornado.
     * @param context O contexto de deserialização.
     * @return Uma instância de Usuario do tipo apropriado.
     * @throws JsonParseException Se o tipo de usuário não for reconhecido.
     */
    public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String tipo = obj.has("tipo") ? obj.get("tipo").getAsString() : "";
        switch (tipo) {
            case "MEMBRO":
                return context.deserialize(json, Membro.class);
            case "ADMIN":
                return context.deserialize(json, Admin.class);
            case "VISITANTE":
                return context.deserialize(json, Visitante.class);
            default:
                throw new JsonParseException("Tipo de usuário desconhecido: " + tipo);
        }
    }
}
