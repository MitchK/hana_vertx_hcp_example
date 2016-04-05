package com.github.mitchk.example;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Servlet implementation class VertxServlet
 */
public class VertxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VertxServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig cfg) {

		Vertx vertx = Vertx.vertx();
		// vertx.createHttpServer().requestHandler(new
		// Handler<HttpServerRequest>() {
		// public void handle(HttpServerRequest req) {
		// System.out.println("Got request: " + req.uri());
		// System.out.println("Headers are: ");
		// for (Map.Entry<String, String> entry : req.headers()) {
		// System.out.println(entry.getKey() + ":" + entry.getValue());
		// }
		// req.response().headers().set("Content-Type", "text/html;
		// charset=UTF-8");
		// req.response().end("<html><body><h1>Hello from
		// vert.x!</h1></body></html>");
		// }
		// }).listen(8888);

		vertx.deployVerticle(HANAVerticle.class.getName(), res -> {
			if (res.succeeded()) {
				System.err.println("Deployment succeeded");
			} else {
				System.err.print(res.cause());
				System.err.println("Deployment failed");
			}
		});

	}

	@Override
	public void destroy() {

	}
}
