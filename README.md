### DB setup

1. Install Database
```
apt-get update -y
apt-get install mariadb-server -y
```

2. Login into the Database
```
sudo mysql -uroot
```
If irs user is already created, you can login with (and password secret4irs):
```
mysql -uirs -p
```

3. Create database
```
CREATE DATABASE test_api;
```
4. Create user (if it doesn't exist)
```
CREATE USER spring@localhost IDENTIFIED BY 'secret';
GRANT ALL PRIVILEGES ON *.* TO 'spring'@'localhost' WITH GRANT OPTION;
"# team-mood-service" 
