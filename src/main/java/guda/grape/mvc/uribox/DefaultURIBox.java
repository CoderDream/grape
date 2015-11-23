package guda.grape.mvc.uribox;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;


public class DefaultURIBox implements URIBox ,InitializingBean{

    private LinkedHashMap<String, String> queryData = new LinkedHashMap<String, String>();

    private String protocol = "http";

    private String host;

    private String path;

    private int port;

    private String originalUrl;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public DefaultURIBox() {

    }

    public DefaultURIBox(DefaultURIBox box) {
        this.host = box.getHost();
        this.path = box.getPath();
        this.port = box.getPort();
        this.protocol = box.getProtocol();
        this.originalUrl = box.getOriginalUrl();
        this.queryData.putAll(box.getQueryData());
    }

    public void copy(DefaultURIBox box) {
        this.host = box.getHost();
        this.path = box.getPath();
        this.port = box.getPort();
        this.protocol = box.getProtocol();
        this.originalUrl = box.getOriginalUrl();
        this.queryData.putAll(box.getQueryData());
    }


    public void setUri(String uri) throws MalformedURLException {
        URL url = new URL(uri);
        this.protocol = url.getProtocol();
        this.host = url.getHost();
        this.port = url.getPort();
        this.path = url.getPath();
        String queryStr = url.getQuery();
        if (StringUtils.hasLength(queryStr)) {
            String[] querys = queryStr.split("&");
            for (int i = 0, len = querys.length; i < len; ++i) {
                if (querys[i].indexOf("=") > 0) {
                    String[] params = querys[i].split("=");
                    queryData.put(params[0], params[1]);
                }
            }
        }

    }

    public DefaultURIBox(String uri) {
        try {
            setUri(uri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }


    public String escapeURL(String uri) {
        return null;
    }


    public String render() {
        StringBuilder buff = new StringBuilder();
        buff.append(protocol).append("://").append(host);
        if (port > -1) {
            buff.append(":").append(port);
        }
        buff.append(path).append("?");
        Set<String> set = queryData.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            buff.append(key).append("=").append(queryData.get(key)).append("&");
        }
        return buff.substring(0, buff.length() - 1);
    }


    public URIBox getURI(String path) {
        if (StringUtils.hasLength(path)) {
            if (path.endsWith("/")&& path.length()>1) {
                path = path.substring(0, path.length() - 1);
            }
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasLength(this.path)) {

                if (!this.path.startsWith("/")) {
                    this.path = "/" + this.path;
                }
                if (this.path.endsWith("/")&& this.path.length()>1) {
                    this.path = this.path.substring(0, path.length() - 1);
                }
                path = this.path + path;
            }
        }
        DefaultURIBox box = new DefaultURIBox();
        box.copy(this);
        box.setPath(path);

        return box;
    }


    public URIBox param(String key, String value) {
        DefaultURIBox box = new DefaultURIBox();
        box.copy(this);
        if (StringUtils.hasLength(key)) {
            box.getQueryData().put(key, value);
        }

        return box;
    }




    @Override
    public String toString() {
        return render();
    }


    public LinkedHashMap<String, String> getQueryData() {
        return queryData;
    }


    public void setQueryData(LinkedHashMap<String, String> queryData) {
        this.queryData = queryData;
    }


    public String getProtocol() {
        return protocol;
    }


    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String addPath(String path) {
        if (StringUtils.hasLength(path)) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (!StringUtils.hasLength(this.path)) {
                this.path = path;
            } else {
                if (!this.path.startsWith("/")) {
                    this.path = "/" + this.path;
                }
                if (this.path.endsWith("/")) {
                    this.path = this.path.substring(0, path.length() - 1);
                }
                this.path = this.path + path;
            }
        }
        return render();
    }


    public URIBox set(String path) {
        if (StringUtils.hasLength(path)) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasLength(this.path)) {

                if (!this.path.startsWith("/")) {
                    this.path = "/" + this.path;
                }
                if (this.path.endsWith("/")) {
                    this.path = this.path.substring(0, path.length() - 1);
                }
                path = this.path + path;
            }
        }
        DefaultURIBox box = new DefaultURIBox();
        box.copy(this);
        box.setPath(path);
        return box;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.hasText(getOriginalUrl())){
            setUri(getOriginalUrl());
        }
    }
}
