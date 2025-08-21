package gent.zeus.guitar.spotify;

public interface ApiTokenGetter {
    String getToken();

    default ApiTokenGetterImpl get() {
        return new ApiTokenGetterImpl();
    }
}
