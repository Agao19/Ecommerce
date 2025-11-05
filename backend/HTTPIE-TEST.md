# Hướng dẫn test API với HTTPie

## Cài đặt HTTPie

### Cách 1: Apt (Ubuntu/Debian)
```bash
sudo apt install httpie
```

### Cách 2: Pip
```bash
pip3 install --user httpie
# Hoặc
sudo pip3 install httpie
```

### Cách 3: Snap
```bash
sudo snap install httpie
```

## Cú pháp HTTPie

```bash
http [METHOD] [URL] [HEADERS] [BODY]
```

## Test các endpoints

### 1. Test Chat đơn giản
```bash
http POST localhost:8080/ai/chat \
  Content-Type:application/json \
  message="What is your name?"
```

### 2. Ingest products vào VectorStore
```bash
http POST localhost:8080/ai/rag/ingest
```

### 3. Test RAG Query đơn giản
```bash
http POST localhost:8080/ai/rag/query-simple \
  Content-Type:application/json \
  message="Tìm iPhone"
```

### 4. Test RAG Query với filter
```bash
http POST localhost:8080/ai/rag/query \
  Content-Type:application/json \
  query="Tìm điện thoại giá cao" \
  categoryId="5" \
  priceRange="high"
```

### 5. Re-index products
```bash
http POST localhost:8080/ai/rag/reindex
```

## Lưu ý

1. **HTTPie tự động set Content-Type: application/json** khi bạn dùng `key=value` hoặc `key:value`
2. **Không cần dấu `:` sau port** - `localhost:8080` không phải `http: 8080`
3. **Method mặc định là GET**, nên phải chỉ định `POST`, `PUT`, `DELETE`
4. **JSON body** có thể viết trực tiếp `key="value"` hoặc `key:=value`

## Ví dụ đầy đủ

```bash
# GET request
http GET localhost:8080/api/products

# POST với JSON body
http POST localhost:8080/ai/chat message="Hello"

# POST với JSON object phức tạp
http POST localhost:8080/ai/rag/query \
  query="Tìm iPhone" \
  categoryId="5" \
  priceRange="high"
```

