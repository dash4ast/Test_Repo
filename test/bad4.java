package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cmdi-00/BenchmarkTest00006")
public class bad4 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // some code
        response.setContentType("text/html;charset=UTF-8");

        String param = "";
        if (request.getHeader("BenchmarkTest00006") != null) {
            param = request.getHeader("BenchmarkTest00006");
        }

        // URL Decode the header value since req.getHeader() doesn't. Unlike req.getParameter().
        param = java.net.URLDecoder.decode(param, "UTF-8");

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        String script = createTaintedScript(param);

        //ruleid: tainted-code-injection-from-http-request-deepsemgrep
        engine.eval(script); //Bad things can happen here.

        String script2 = "this is a hardcoded script";
        // ok: tainted-code-injection-from-http-request-deepsemgrep
        engine.eval(script2); //Bad things can happen here.

        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        //ruleid: tainted-code-injection-from-http-request-deepsemgrep
        ValueExpression vex = expressionFactory.createValueExpression(elContext, "expression" + param, String.class);

        String result = evaluateExpression("expression" + param);

    }

    public String createTaintedScript(String param){
        return "this is some script" + param;
    }

    public String evaluateExpression(String expression) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        // deepid: tainted-code-injection-from-http-request-deepsemgrep
        ValueExpression vex = expressionFactory.createValueExpression(elContext, expression, String.class);
        return (String) vex.getValue(elContext);
    }

    public void test(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String param = "";
        if (request.getHeader("BenchmarkTest00006") != null) {
            param = request.getHeader("BenchmarkTest00006");
        }
        // URL Decode the header value since req.getHeader() doesn't. Unlike req.getParameter().
        param = java.net.URLDecoder.decode(param, "UTF-8");
        // ruleid: tainted-code-injection-from-http-request-deepsemgrep
        MVEL.eval(param);
    }

    public void test2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Velocity.init();

        String code = request.getParameter("code");

        VelocityContext context = new VelocityContext();

        context.put("test1", "test2");
        context.put("test3", "test4");

        StringWriter w = new StringWriter();
        // ruleid: tainted-code-injection-from-http-request-deepsemgrep
        Velocity.evaluate(context, w, "test5", code);
    }

    @GetMapping(value = "good")
	public void test3(HttpServletRequest request) {
		Velocity.init();
		VelocityContext context = new VelocityContext();

		context.put("test1", "test2");
		context.put("test3", "test4");

		String s = "command $project";
        StringWriter w = new StringWriter();
        // ok: tainted-code-injection-from-http-request-deepsemgrep
		Velocity.evaluate(context, w, "test5", s);
    }
    
    @GetMapping(value = "good")
	public void test4(HttpServletRequest request) {
        String code = request.getParameter("code");
        JexlEngine jexl = new JexlBuilder().create();
        // ruleid: tainted-code-injection-from-http-request-deepsemgrep
        JexlExpression expression = jexl.createExpression(code);
        JexlContext context = new MapContext();
        expression.evaluate(context);
	}
}
