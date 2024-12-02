FROM python:3.9-slim

WORKDIR /app

COPY bank.py /app/

RUN pip install flask

EXPOSE 5000

CMD ["python", "bank.py"]