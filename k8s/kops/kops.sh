
# make sure on .bashrc file we have:
# export PATH=$PATH:/usr/local/bin/

# 1. Setup IAM
# then run aws configure & login

CLUSTER_NAME="verifiko.k8s.local"
S3_BUCKET="verifiko.k8s.local-state" #this thing has to be unique
AWS_REGION="ap-southeast-2"
ZONES="ap-southeast-2a,ap-southeast-2b,ap-southeast-2c"

# 2. Installing Kops/Kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
wget https://github.com/kubernetes/kops/releases/download/v1.34.0/kops-linux-amd64
chmod +x kops-linux-amd64 kubectl
sudo mv kubectl /usr/local/bin/kubectl
sudo mv kops-linux-amd64 /usr/local/bin/kops

# 3. create s3 bucket (storing our config for k8s)
aws s3api create-bucket \
  --bucket ${S3_BUCKET} \
  --region ${AWS_REGION} \
  --create-bucket-configuration LocationConstraint=${AWS_REGION}

aws s3api put-bucket-versioning --bucket ${S3_BUCKET} \
  --region ${AWS_REGION} \
  --versioning-configuration Status=Enabled

export KOPS_STATE_STORE=s3://${S3_BUCKET}

# 4. creating cluster
kops create cluster \
  --name ${CLUSTER_NAME} \
  --zones ${ZONES} \
  --master-count=1 \
  --master-size t3.medium \
  --node-count=3 \
  --node-size t3.small

# kops update cluster --name verifiko.k8s.local --yes --admin