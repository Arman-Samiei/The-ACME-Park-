from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/payslip/request', methods=['POST'])
def process_payment():
    data = request.json
    staffId = data.get("staffId")
    paymentAmount = data.get("paymentAmount")

    if not all([staffId, paymentAmount]):
        return jsonify({"success": False}), 400

    # Check if staff starts with a
    if staffId.startswith('a'):
        return jsonify({"success": False}), 200

    # Otherwise, approve the payment
    return jsonify({"success": True}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)