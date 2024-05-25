# Todolist 2 Layer BE

### Cách kết nối

- Chạy trên port `6040` của server Eledevo
    - Connect in local: `192.168.1.5:6040`
    - Connect from internet: `<Public IP or Domain>:6040`

### Java
- Java version 17

### For DevOps

```bash
cd /var/lib/jenkins/workspace/

cd /Todolist 2 Layer BE/target

docker run --name todolist-2-layer-be-container -d -p 6040:8080 -v "$(pwd):/app" todolist-2-layer-be:1.0.0
```
