package proxy;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import java.net.URL;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RedirectFilter extends ZuulFilter {

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 10;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext context = RequestContext.getCurrentContext();
    Map<String, String[]> parameterMap = context.getRequest().getParameterMap();

    String url = context.getRequest().getHeader("turl");
    if (url == null) {
      throw new IllegalStateException("URL is null");
    }

    String authenticatedKey = "routeHost";

    try {
      context.set(authenticatedKey, new URL(url));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
