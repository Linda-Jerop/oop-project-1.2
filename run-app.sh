#!/bin/bash
# Runs the Strathmore Room Finder. Usage:  ./run-app.sh   (from the project folder)
# It starts MySQL first if MySQL is not already running, then opens the app.

# Go to the folder that contains this script (the project folder), wherever we were started from.
cd "$(dirname "$0")" || exit 1

# This computer only has a JRE, so Maven cannot compile. We use the JDK that comes with the VS Code Java extension.
VSCODE_JDK=$(ls -d "$HOME"/.vscode/extensions/redhat.java-*/jre/*/ 2>/dev/null | head -1)
if [ -n "$VSCODE_JDK" ]; then
    export JAVA_HOME="$VSCODE_JDK"
fi

# This checks if something is listening on the MySQL port (3306).
mysql_is_running() {
    (echo > /dev/tcp/127.0.0.1/3306) 2>/dev/null
}

if mysql_is_running; then
    echo "MySQL is already running."
elif [ -x "$HOME/mysql/start-mysql.sh" ]; then
    echo "MySQL is not running. Starting it..."
    # setsid + nohup keep MySQL running after you close this terminal. Its messages go to start.log.
    setsid nohup "$HOME/mysql/start-mysql.sh" > "$HOME/mysql-data/start.log" 2>&1 &
    for i in $(seq 1 30); do
        mysql_is_running && break
        sleep 1
    done
    if ! mysql_is_running; then
        echo "MySQL did not start. Look at $HOME/mysql-data/mysql-error.log"
        exit 1
    fi
    echo "MySQL is running."
else
    echo "MySQL is not running and $HOME/mysql/start-mysql.sh was not found."
    echo "Start your MySQL server first, then run this script again."
    exit 1
fi

echo "Starting the app..."
mvn -q javafx:run
