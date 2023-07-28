package com.exam.sbb;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {

  private int increaseNo = -1;

  @RequestMapping("/sbb")
  @ResponseBody
  public String index() {
    return "안녕하세요.!!!!";
  }

  @GetMapping("/page1")
  @ResponseBody
  public String showGet() {
    return """
        <form method="POST" action="/page2">
           <input type="number" name="age" placeholder="나이 입력" />
           <input type="submit" value="page2로 POST 방식으로 이동" />
        </form>
        """;
  }

  @PostMapping("/page2")
  @ResponseBody
  public String showPage2Post(@RequestParam(defaultValue = "0") int age) {
    return """
        <h1>입력된 나이 : %d</h1>
        <h1>안녕하세요. POST 방식으로 오신걸 환영합니다.</h1>
        """.formatted(age);
  }

  @GetMapping("/page2")
  @ResponseBody
  public String showPage2Get(@RequestParam(defaultValue = "0") int age) {
    return """
        <h1>입력된 나이 : %d</h1>
        <h1>안녕하세요. POST 방식으로 오신걸 환영합니다.</h1>
        """.formatted(age);
  }

  @GetMapping("/plus")
  @ResponseBody
  public int showPlus(int a, int b) {
    return a + b;
  }

  @GetMapping("/minus")
  @ResponseBody
  public int showMinus(int a, int b) {
    return a - b;
  }

  @GetMapping("/increase")
  @ResponseBody
  public int showIncrease() {
    increaseNo++;
    return increaseNo;
  }

  @GetMapping("/gugudan")
  @ResponseBody
  public String showIncrease(Integer dan, Integer limit) {
    if (dan == null) {
      dan = 9;
    }

    if (limit == null) {
      limit = 9;
    }

    Integer finalDan = dan;
    return IntStream.rangeClosed(1, limit)
        .mapToObj(i -> "%d * %d =%d".formatted(finalDan, i, finalDan * i))
        .collect(Collectors.joining("<br>\n"));
  }

  @GetMapping("/plus2")
  @ResponseBody
  public void showPlus2(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int a = Integer.parseInt(req.getParameter("a"));
    int b = Integer.parseInt(req.getParameter("b"));

    resp.getWriter().append(a + b + "");
  } //서블릿 방식 Springboot방식비교

  /*
    홍길동 =INFP
    홍길순 =ENFP
    임꺽정 =ESFJ
    본인 =INFJ
   */
//  @GetMapping("/mbti/{name}")
//  @ResponseBody
//  public String  mbti(@PathVariable String name) {
//    return switch (name) {
//      case "홍길동" -> "INFP";
//      case "홍길순" -> "ENFP";
//      case "임꺽정" -> "ESFJ";
//      case "김청일" -> "INFJ";
//      default -> "모름";
//    }; //이런방식으로 사용가능
//  }
  @GetMapping("/mbti/{name}")
  @ResponseBody
  public String mbti(@PathVariable String name) {
    return switch (name) {
      case "홍길순" -> {
        char j = 'J';
        yield "IFN" + j;
      }
      case "임꺽정" -> "ESFJ";
      case "홍길동, 김청일" -> "INFJ";
      default -> "모름";
    }; // yield = return 들어가면 헷갈려서 yield사용
  }

  @GetMapping("/saveSession/{name}/{value}")
  @ResponseBody
  public String saveSession(@PathVariable String name, @PathVariable String value, HttpServletRequest req) {
    HttpSession session = req.getSession();

    session.setAttribute(name, value);

    return "세션변수 %s의 값이 %s(으)로 설정되었습니다.".formatted(name, value);
  }

  @GetMapping("/getSession/{name}")
  @ResponseBody
  public String getSession(@PathVariable String name, HttpSession session) {
    //HttpSession session = req.getSession();

    // req => 쿠키 => JSESSIONID => 세션을 얻을 수 있다.
    String value = (String)session.getAttribute(name);

    return "세션변수 %s의 값이 %s 입니다.".formatted(name, value);
  }

  private List<Article> articles = new ArrayList<>();

  @GetMapping("/addArticle")
  @ResponseBody
  public String addArticle(String title, String body) {
    Article article = new Article(title, body);

    articles.add(article);

    return "%d번 게시물이 생성되었습니다.".formatted(article.getId());
  }

  @GetMapping("/getArticle/{id}")
  @ResponseBody
  public Article getArticle(@PathVariable int id) {
    Article article = articles
        .stream()
        .filter(a -> a.getId() == id)
        .findFirst()
        .get();

    return article;
  }
}

@AllArgsConstructor
@Getter
class Article {
  private  static  int lastId = 0;
  private final int id;
  private final String title;
  private final String body;

  public  Article(String title, String body) {
    this(++lastId, title, body);
  }
}
