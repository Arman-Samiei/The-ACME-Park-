from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/bank/request', methods=['POST'])
def process_payment():
    data = request.json
    ccNumber = data.get("ccNumber")
    ccExpiry = data.get("ccExpiry")
    ccCVC = data.get("ccCVC")
    paymentAmount = data.get("paymentAmount")

    if not all([ccNumber, ccExpiry, ccCVC, paymentAmount]):
        return jsonify({"success": False}), 400

    # Check if the credit card number starts with 1
    if ccNumber.startswith('1'):
        return jsonify({"success": False}), 200

    # Otherwise, approve the payment
    return jsonify({"success": True}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)