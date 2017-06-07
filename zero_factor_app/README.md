# xp_problem_set_zero_factor_app

You just joined Brent's team. Brent has been working on a Shopping Cart app, and he's pretty pleased with what he has come up with... there's only a few features to add and a couple of known-issues in the backlog. Unfortunately Brent is out sick today, and the app needs to be released tomorrow, so you'll have to work through the backlog and ship the app!

## Setup

### Application Server

Brent has left instructions on how to set up a development environment, and it looks pretty easy:

```
brew install tomcat
catalina start
```

Tomcat is a Java application server, and this application runs inside of it. Catalina is the "codename" for tomcat, so you can start and stop it using those commands.

### Monitoring/Logging

Brent thinks IDEs are the tools of the devil, so Brent only "debugs" using vim and his favorite logger, log4j.

Brent views the tomcat logs using this command:

```
tail -f  /usr/local/Cellar/tomcat/8.5.9/libexec/logs/catalina.out
```

And he tails his apps logs like so:

```
tail -f  /usr/local/Cellar/tomcat/8.5.9/libexec/logs/shopcart.out
```

### Build/Deployment

Brent doesn't trust new-fangled build tools like maven or gradle, so he's created his own highly optimized build scripts:

- build.sh
- deploy.sh

These are located in the `app/` directory, along with all of the code for the site. (There is also an `admin/` folder which we will explain in a minute.)

You can use those to compile and run the app. Tomcat listens on port 8080, so you can see the management console here:

```
http://localhost:8080/
```

And the app itself is here:

```
http://localhost:8080/shopcart/
```

### Data(base)

Brent thinks databases are silly overblown scams that vendors like Oracle use to lock-in companies like yours. He really prefers JSON, so he keeps all his data in JSON files in the `db/` folder, which he also checks into git. Some of the data is static, like the list of items, but other data is more dynamic, e.g. what's in your cart at the present moment.

The only problem is that sometimes the `cart.json` file seems to get corrupted, so until he can work out the bug, he's created a second app, in the `admin/` folder that he runs from the command line to restore the database to a known-good state whenever the corruption happens.

### Reporting

The final piece of functionality worth discussing is report generation. Once the first order has been placed, the app starts a timer, and polls for a file in the `db` directory called `generate`. If the app finds that file, it deletes it and generates a report in it's place (`db/report.txt`). To test this, Brent runs the following commands:

```
touch db/generate
```

The above command puts a request in the queue. At some future point, the report will be placed in the `db/` folder, so Brent runs `ls -l db` until he sees that it's there, then finally:

```
cat db/report.txt
```

To see how much money we've made that day.

### Backlog

Answer the following questions, and complete any resulting tasks _on the existing codebase_:

1. `Config`: Where is the config stored for this app? Does it have any system properties hard coded? Refactor the app to turn any hard-coded config into environmental config.
1. `Concurrency`: Is the app set up for concurrency? Can two users use it at the same time? If not, refactor the app to allow multiple users to have simultaneous access.
1. `Dev/prod parity`: Does the app act the same way in dev as it does in production? If not, refactor it so that it is unaware and unconcerned with it's present environment.
1. `Logs`: Are the logs an unbuffered stream written to `stdout`? If not, refactor to make it so!
1. `Admin processes`: If there are admin tasks, are the built into the app itself? If not, make it so!
1. `Port binding`: Does this app provide any services that can't be triggered through a request to a port? If so, refactor the code so the same functionality is present purely through TCP/IP.

### Productionization

In order to get the app deployed to production, the platform team wants you to make it 12 factor compliant:

1. `Dependencies`: What are the dependencies of this app? Are they explicitly defined in a manifest? Can you think of a build system that would bring us into compliance with the 12 factor principles?
1. `Backing Services`: Does this app have any backing services? If so, are they treated like attached resources? Can you think of a framework that would allow us to treat the services like attached resources?
1. `Build, release, run`: Does Brent's `build.sh` and `deploy.sh` get us a unique version number? Does it combine config with artifacts? Finally, does it start the app by invoking a process? If not, can you think of a better way to handle this?
1. `Processes`: Does the app consist of one or more stateless processes? If not, do you know how you would externalize the state to satisfy the "Processes" principle?
1. `Concurrency`: Will the app scale horizontally? Will it run safely on more than one server without resorting to "sticky sessions"? If not, how could you solve this problem?
1. `Disposability`: Is the app disposable? If killed at any point in time, could it gracefully recover? What could be done to make sure the database never gets corrupted?

In light of the changes necessary for the platform team, should this be a rewrite or a refactor? Make your decision and rewrite (or refactor) this app so that it is fully 12 factor compliant. (Hint, have we covered any frameworks that might help with this?)
