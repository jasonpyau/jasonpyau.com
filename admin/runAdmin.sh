#!/bin/bash

# Use to easily run admin panel

function compile() {
    javac AdminPanel.java &
    wait

    if [ $? -ne 0 ]; then 
        echo "Compile of AdminPanel.java failed"
        exit 1
    fi
}

function run() {
    java AdminPanel
}

function clean() {
    rm -f *.class
}

script_dir=$(dirname "$(realpath $0)")

if [ ! -f "$script_dir/AdminPanel.java" ]; then
    echo "$script_dir/AdminPanel.java does not exist. Exiting..."
    exit 1
fi
if [ ! -f "$script_dir/AdminPanel.properties" ]; then
    echo "$script_dir/AdminPanel.properties does not exist. Exiting..."
    exit 1
fi

cd "$script_dir"

case ${1} in
    compile)
        compile
    ;;
    run)
        run
    ;;
    clean)
        clean
    ;;
    help)
        echo "[compile] - Compile the Admin Panel"
        echo "[run] - Run the Admin Panel"
        echo "[clean] - Remove class files"
        echo "[default] - Compile, run, and clean"
    ;;
    *)
        compile
        run
        clean    
esac
