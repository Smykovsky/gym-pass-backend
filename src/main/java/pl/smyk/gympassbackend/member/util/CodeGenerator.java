package pl.smyk.gympassbackend.member.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.util.Base64;

@Service
public class CodeGenerator {
    public String generateQRCode(String data) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String encrypted = AESUtils.encrypt(data);
        BitMatrix bitMatrix = qrCodeWriter.encode(encrypted, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(pngData);
    }

//    public String generateBarcode(HashMap<String, Object> obj) throws WriterException, IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String json = objectMapper.writeValueAsString(obj);
//
//        Code128Writer barcodeWriter = new Code128Writer();
//        BitMatrix bitMatrix = barcodeWriter.encode(json, BarcodeFormat.CODE_128, 300, 300);
//        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//        byte[] pngData = pngOutputStream.toByteArray();
//        return Base64.getEncoder().encodeToString(pngData);
//    }
}
