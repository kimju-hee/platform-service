FROM gitpod/workspace-full

# 타임존 설정
ENV TZ=Asia/Seoul
RUN sudo apk add --no-cache tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ | sudo tee /etc/timezone

# Helm 설치
RUN curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash

# kubectl 설치
RUN sudo apt-get update && sudo apt-get install -y apt-transport-https ca-certificates curl && \
    curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add - && \
    echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee -a /etc/apt/sources.list.d/kubernetes.list && \
    sudo apt-get update && \
    sudo apt-get install -y kubectl

# Azure CLI 설치
RUN curl -sL https://aka.ms/InstallAzureCLIDeb | bash

# Java 17, Maven, Docker 설치 (이미 포함돼있을 수도 있음)
RUN sudo apt-get install -y openjdk-17-jdk maven docker.io

# init.sh 복사
COPY init.sh /init.sh
RUN chmod +x /init.sh
