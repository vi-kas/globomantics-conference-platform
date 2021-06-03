package com.globomantics.conference.util

import scalaz.Memo
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object PasswordHasher {

  import com.github.t3hnar.bcrypt._

  val memoizedBcrypt: String => Future[String] =
    Memo.immutableHashMapMemo {

      pwd => Future {
        pwd.bcrypt
      }
    }
}
