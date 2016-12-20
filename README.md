# scala-blog
Study project for course "Reactive Scala" in SPbAU

## Prerequisites

App expects MySQL instance running on `localhost:3306`, with db
`blog-db` and user `root@root` with read/write privileges for that db.

Address, db name, username and password can be corrected as usual, in `application.conf`

## Launching

```
$ sbt runWithJS
```
And wait, wait, wait...


## Using app

User with id = 1 (in MySQL it is the first ID assigned) considered to be admin
  by app. When logged in, additional helper "Add post" will appear (only for that user!)
  Of course, one can add posts manually, working directly with DB.
