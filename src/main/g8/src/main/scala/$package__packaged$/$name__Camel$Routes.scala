package $package$

import cats.effect.{Blocker, ContextShift, Sync}
import cats.implicits._
import org.http4s.{HttpRoutes, Request, StaticFile, Uri}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.{Location}

object $name;format="Camel"$Routes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      /**
       * @api {get} /joke Request a funny joke
       * @apiName GetJoke
       * @apiGroup Joke
       * @apiVersion 0.1.0
       *
       * @apiSuccess {String} message Message to greet the user.
       *
       * @apiSuccessExample Success-Response:
       *     HTTP/1.1 200 OK
       *     {
       *       "joke": "\"Dad, I'm hungry.\" Hello, Hungry. I'm Dad."
       *     }
       */
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      /**
       * @api {get} /hello/:name Request User information
       * @apiName GreetUser
       * @apiGroup Greeting
       * @apiVersion 0.1.0
       *
       * @apiParam {String} name Users unique ID.
       *
       * @apiSuccess {String} message Message to greet the user.
       *
       * @apiSuccessExample Success-Response:
       *     HTTP/1.1 200 OK
       *     {
       *       "message": "Hello, user"
       *     }
       */
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }

  def apidocRoutes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "apidoc" =>
      TemporaryRedirect(Location(Uri.uri("apidoc/index.html")))
    }
  }

  // Serve static files see https://http4s.org/v0.21/static/
  def staticRoutes[F[_]: ContextShift: Sync](blocker: Blocker): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    def static(blocker: Blocker, request: Request[F]) =
      StaticFile
        .fromResource(request.pathInfo, blocker, Some(request))
        .getOrElseF(NotFound())
    val supportedStaticExtensions =
      List(".html", ".js", ".map", ".css", ".png", ".ico", ".jpg", ".jpeg", ".otf", ".ttf" )
      HttpRoutes.of[F] {
        // Loads Any Static Resources as Called
        case req if supportedStaticExtensions.exists(req.pathInfo.endsWith) =>
        static(blocker, req)
      }
  }
}