package zio.http.api

import zio.http._
import zio.test._

object OriginMiddlewareSpec extends ZIOSpecDefault {
  val response      = Response.ok
  override def spec =
    suite("OriginMiddlewareSpec")(
      suite("valid values")(
        test("add valid https value for Origin") {
          for {
            response <- api.Middleware
              .withOrigin("https://domain")
              .apply(Http.succeed(response))
              .apply(Request.get(URL.empty))
          } yield assertTrue(response.headers.origin.getOrElse("error").equals("https://domain:443"))
        },
        test("add valid http value for Origin") {
          for {
            response <- api.Middleware
              .withOrigin("http://domain")
              .apply(Http.succeed(response))
              .apply(Request.get(URL.empty))
          } yield assertTrue(response.headers.origin.getOrElse("error").equals("http://domain:80"))
        },
      ),
      suite("invalid values")(
        test("add invalid value for Origin") {
          for {
            response <- api.Middleware
              .withOrigin("$%#@#%")
              .apply(Http.succeed(response))
              .apply(Request.get(URL.empty))
          } yield assertTrue(response.headers.origin.getOrElse("error").equals(""))
        },
      ),
    )
}
