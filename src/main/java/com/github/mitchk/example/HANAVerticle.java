package com.github.mitchk.example;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HANAVerticle extends AbstractVerticle {

	private JDBCClient client;

	private void helloWorld(RoutingContext routingContext) {
		client.getConnection(res -> {
			if (res.succeeded()) {

				SQLConnection connection = res.result();

				connection.query("SELECT 'Hello World' AS GREETING FROM DUMMY", res2 -> {
					if (res2.succeeded()) {

						ResultSet rs = res2.result();

						routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
								.end(Json.encodePrettily(rs));
					} else {
						System.err.println(res2.cause());
						JsonObject obj = new JsonObject();
						obj.put("error", res2.cause().getMessage());

						routingContext.response().setStatusCode(500)
								.putHeader("content-type", "application/json; charset=utf-8")
								.end(Json.encodePrettily(obj));
					}
				});
			} else {
				System.err.println(res.cause());
				JsonObject obj = new JsonObject();
				obj.put("error", res.cause().getMessage());

				routingContext.response().setStatusCode(500)
						.putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(obj));
			}
		});

	}

	@Override
	public void start(Future<Void> fut) {

		InitialContext ctx;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");

			client = JDBCClient.create(vertx, ds);

			Router router = Router.router(vertx);

			router.get("/api/helloWorld").handler(this::helloWorld);

			vertx.createHttpServer().requestHandler(router::accept).listen(
					// Retrieve the port from the configuration,
					// default to 8080.
					config().getInteger("http.port", 8888), result -> {
						if (result.succeeded()) {
							fut.complete();
						} else {
							fut.fail(result.cause());
						}
					});
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
