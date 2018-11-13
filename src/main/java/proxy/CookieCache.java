package proxy;

import java.util.Collection;
import okhttp3.Cookie;

public interface CookieCache extends Iterable<Cookie> {
  void addAll(Collection<Cookie> var1);

  void clear();
}
