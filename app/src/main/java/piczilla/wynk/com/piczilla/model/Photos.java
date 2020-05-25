package piczilla.wynk.com.piczilla.model;

public class Photos {
    private String farm;
    private String server;
    private String id;
    private String secret;

    private Photos(String farm, String server, String id, String secret) {
        this.farm = farm;
        this.server = server;
        this.id = id;
        this.secret = secret;
    }

    public static Photos getObject(String farm, String server, String id, String secret) {
        return new Photos(farm, server, id, secret);
    }

    public String getFarm() {
        return farm;
    }

    public String getServer() {
        return server;
    }

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }
}
