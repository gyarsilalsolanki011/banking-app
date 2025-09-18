#!/bin/sh
set -e

host="$1"       # mysql-banking
shift
cmd="$@"        # java -jar /app.jar

echo "⏳ Waiting for MySQL ($host) to be ready..."

# Max wait 60 seconds
timeout=60
counter=0

until mysql -h "$host" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "USE ${MYSQL_DATABASE}; SELECT 1;" >/dev/null 2>&1; do
  counter=$((counter+2))
  if [ $counter -ge $timeout ]; then
    echo "❌ Timeout reached. MySQL is not ready after $timeout seconds."
    exit 1
  fi
  echo "🔄 Still waiting for MySQL ($counter sec)..."
  sleep 2
done

echo "✅ MySQL is ready. Starting application..."
exec $cmd
