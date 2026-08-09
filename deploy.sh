#!/bin/bash

echo "=============================="
echo "  PartTimeGo Docker 部署脚本  "
echo "=============================="

# 1. 打包后端
echo "[1/4] 打包后端项目..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "后端打包失败！"
    exit 1
fi

# 2. 停止旧容器
echo "[2/4] 停止旧容器..."
docker-compose down

# 3. 构建并启动
echo "[3/4] 构建并启动容器..."
docker-compose up -d --build

if [ $? -ne 0 ]; then
    echo "容器启动失败！"
    exit 1
fi

# 4. 等待启动
echo "[4/4] 等待服务启动..."
sleep 10

# 检查状态
echo ""
echo "=============================="
echo "  部署完成！"
echo "=============================="
echo ""
echo "前端访问: http://localhost"
echo "后端接口: http://localhost:9090"
echo ""
echo "查看日志: docker-compose logs -f"
echo "停止服务: docker-compose down"
echo ""
