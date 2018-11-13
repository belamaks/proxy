package proxy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import okhttp3.Cookie;

public class CookieHashSet implements CookieCache {
  private Set<CookieBox> boxes = new HashSet();

  public CookieHashSet() {
  }

  public void addAll(Collection<Cookie> cookies) {
    Iterator var2 = cookies.iterator();

    while(var2.hasNext()) {
      Cookie cookie = (Cookie)var2.next();
      CookieBox box = new CookieBox(cookie);
      this.boxes.remove(box);
      this.boxes.add(box);
    }

  }

  public void clear() {
    this.boxes.clear();
  }

  public Iterator<Cookie> iterator() {
    return new CookieHashSet.CookieHashSetIterator();
  }

  private class CookieHashSetIterator implements Iterator<Cookie> {
    private Iterator<CookieBox> iterator;

    CookieHashSetIterator() {
      this.iterator = CookieHashSet.this.boxes.iterator();
    }

    public boolean hasNext() {
      return this.iterator.hasNext();
    }

    public Cookie next() {
      return ((CookieBox)this.iterator.next()).getCookie();
    }

    public void remove() {
      this.iterator.remove();
    }
  }
}