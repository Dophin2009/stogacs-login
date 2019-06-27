package net.edt.web.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import net.edt.web.exception.QRGenerationFailedException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRService {

    public byte[] generateQRCode(String toEncode) {
        QRCodeWriter qrWriter = new QRCodeWriter();
        try {
            BitMatrix matrix = qrWriter.encode(toEncode, BarcodeFormat.QR_CODE, 900, 900);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            return output.toByteArray();
        } catch (WriterException | IOException ignored) {
            throw new QRGenerationFailedException("QR code generation failed");
        }
    }

}
