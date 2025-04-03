<?php
include 'config.php';

$data = json_decode(file_get_contents("php://input"));

$name = $data->name;
$age = $data->age;
$gpa = $data->gpa;
$email = strtolower(str_replace(" ", "", $name)) . "@vku.udn.vn";

$sql = "INSERT INTO Student (name, age, email, gpa) VALUES ('$name', $age, '$email', $gpa)";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["message" => "Thêm sinh viên thành công!", "email" => $email]);
} else {
    echo json_encode(["error" => "Lỗi: " . $conn->error]);
}

$conn->close();
?>
