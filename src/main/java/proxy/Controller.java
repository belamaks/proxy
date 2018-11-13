package proxy;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
public class Controller {

  private static Set<String> keys = new HashSet<>();

  static {
    keys.add("host");
    keys.add("turl");
    keys.add("cache-control");
    keys.add("postman-token");
    keys.add("accept");
    keys.add("accept-encoding");
  }

  @SneakyThrows
  @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
  public void request(HttpServletRequest request, HttpServletResponse response) {
    OkHttpClient okHttpClient = createOkHttpClient();
    String url = request.getHeader("turl");

    Builder builder = new Builder().url(url);

    if (!request.getMethod().equals("GET")) {
      StringWriter writer = new StringWriter();
      IOUtils.copy(request.getInputStream(), writer, "UTF-8");
      String body = writer.toString();
      RequestBody requestBody = RequestBody.create(MediaType.get(request.getContentType()), body);
      builder.method(request.getMethod(), requestBody);
    }

    Enumeration<String> headerNames = request.getHeaderNames();

    while (headerNames.hasMoreElements()) {
      String key = headerNames.nextElement();
      if (!keys.contains(key.toLowerCase())) {
        builder.addHeader(key, request.getHeader(key));
      }
    }

    Request newRequest = builder.build();
    Response execute = okHttpClient.newCall(newRequest).execute();

    String result = execute.body().string();

    response.setStatus(execute.code());

    execute.headers().toMultimap().forEach((s, strings) -> {
      response.setHeader(s, strings.get(0));
    });

    response.getWriter().write(result);
    response.getWriter().flush();
    response.getWriter().close();

  }

  public static OkHttpClient createOkHttpClient() {
    return new OkHttpClient.Builder()
        .cookieJar(new DefaultCookieJar(new CookieHashSet()))
        .build();
  }
}
