package guda.grape.mvc.uribox;


public interface URIBox {

    String escapeURL(String uri);

    public String render();

    public URIBox getURI(String path);

    public String addPath(String path);

    public URIBox param(String key, String value);

}
