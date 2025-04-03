<?php
include 'config.php';

$data = json_decode(file_get_contents("php://input"));
$email = $data->email;

$sql = "SELECT * FROM Student WHERE email='$email'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    echo json_encode(["message" => "Đăng nhập thành công!", "email" => $email]);
} else {
    echo json_encode(["error" => "Email không tồn tại!"]);
}

$conn->close();
?>
