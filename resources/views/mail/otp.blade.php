<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verification Code</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f4f4; margin: 0; padding: 0; }
        .container { max-width: 480px; margin: 40px auto; background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
        .header { background: #1a2c5b; padding: 32px 40px; text-align: center; }
        .header h1 { color: #fff; margin: 0; font-size: 22px; letter-spacing: 1px; }
        .body { padding: 36px 40px; }
        .body p { color: #444; font-size: 15px; line-height: 1.6; margin: 0 0 16px; }
        .code-box { background: #f0f4ff; border: 2px dashed #1a2c5b; border-radius: 10px; text-align: center; padding: 24px; margin: 28px 0; }
        .code-box span { font-size: 42px; font-weight: bold; letter-spacing: 14px; color: #1a2c5b; }
        .expiry { color: #888; font-size: 13px; text-align: center; margin-top: -12px; }
        .footer { background: #f8f8f8; padding: 20px 40px; text-align: center; }
        .footer p { color: #aaa; font-size: 12px; margin: 0; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Pathlingo</h1>
        </div>
        <div class="body">
            <p>Hi {{ $name }},</p>
            <p>Use the code below to verify your email address. This code expires in <strong>10 minutes</strong>.</p>
            <div class="code-box">
                <span>{{ $code }}</span>
            </div>
            <p class="expiry">Do not share this code with anyone.</p>
            <p>If you did not create a Pathlingo account, you can safely ignore this email.</p>
        </div>
        <div class="footer">
            <p>&copy; {{ date('Y') }} Pathlingo. All rights reserved.</p>
        </div>
    </div>
</body>
</html>
