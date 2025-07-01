#!/bin/bash
set -e

# 기본 툴 설치
echo "[0] 유틸리티 설치 (기본 툴)"
sudo apt-get update
sudo apt-get install -y curl apt-transport-https ca-certificates gnupg lsb-release

# httpie 설치
echo "[1] httpie 설치"
pip install --upgrade pip
pip install httpie

# Azure CLI 설치
echo "[2] Azure CLI 설치"
curl -sL https://packages.microsoft.com/keys/microsoft.asc | \
  gpg --dearmor | \
  sudo tee /etc/apt/trusted.gpg.d/microsoft.gpg > /dev/null

echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ jammy main" | \
  sudo tee /etc/apt/sources.list.d/azure-cli.list

sudo apt-get update
sudo apt-get install -y azure-cli

# Helm 설치
echo "[3] Helm 설치"
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash

# Maven Snapshot 빌드
echo "[4] Maven snapshot 빌드"
mvn clean package -DskipTests

# Kafka 도구 다운로드 및 PATH 설정 (현재는 Kafka를 Docker Compose로 실행 중이므로 이 부분은 수정)
# echo "[5] Kafka 도구 다운로드 및 PATH 설정"
# mkdir -p ~/kafka
# cd ~/kafka
#
# if [ ! -d "kafka_2.13-3.7.0" ]; then
#   echo "Kafka 압축파일 다운로드 중..."
#   wget https://archive.apache.org/dist/kafka/3.7.0/kafka_2.13-3.7.0.tgz
#   tar -xzf kafka_2.13-3.7.0.tgz
# fi
#
# export KAFKA_HOME=~/kafka/kafka_2.13-3.7.0
# export PATH=$KAFKA_HOME/bin:$PATH
# echo "Kafka CLI PATH 설정 완료: $(which kafka-console-producer.sh)"

# Kafka docker-compose 실행
echo "[6] Kafka docker-compose 실행"

# 프로젝트 디렉토리로 이동
if [ -f "/workspace/platform-service/docker-compose.yml" ]; then
  cd /workspace/platform-service
  docker-compose up -d
else
  echo "docker-compose.yml 파일을 찾을 수 없습니다: /workspace/platform-service"
fi

echo "모든 초기 설정 완료!"
