package proxy;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class DefaultCookieJar implements CookieJar {
  private CookieCache cache;

  public DefaultCookieJar(CookieCache cache) {
    this.cache = cache;
  }

  public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
    this.cache.addAll(cookies);
  }

  public synchronized List<Cookie> loadForRequest(HttpUrl url) {
    List<Cookie> cookies = new ArrayList();
    Iterator it = this.cache.iterator();

    while(it.hasNext()) {
      Cookie cookie = (Cookie)it.next();
      if (cookie.expiresAt() < System.currentTimeMillis()) {
        it.remove();
      } else if (cookie.matches(url)) {
        cookies.add(cookie);
      }
    }

    return cookies;
  }
}
