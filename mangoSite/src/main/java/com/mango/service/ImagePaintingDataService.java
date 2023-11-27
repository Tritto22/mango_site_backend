package com.mango.service;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mango.dto.ErrorDto;
import com.mango.dto.ImagePaintingDataDto;
import com.mango.dto.ResponseDto;
import com.mango.entity.ImagePaintingData;
import com.mango.repository.ImagePaintingRepository;

import ch.qos.logback.classic.pattern.Util;

@Service
public class ImagePaintingDataService {

	@Autowired
    private ImagePaintingRepository imageRepository;
	
	// PUSH
	public ResponseDto uploadImagePainting(MultipartFile file){
		
		var response = new ResponseDto();
		
		try {
			var image = new ImagePaintingData();
			image.setName(file.getOriginalFilename());
			image.setType(file.getContentType());
			image.setImageData(ImagePaintingDataService.compressImage(file.getBytes()));
			
			imageRepository.save(image);
			
			response.setPayload("file aggiunto con successo" + file.getOriginalFilename());
		} catch(Exception e) {
			
			ErrorDto error = new ErrorDto();
			error.setMsg("Errore nell'upload dell'immagine del quadro!");
		}
				
		return response;
	}
	
	// GET 
	@Transactional
    public ResponseDto getInfoByImageByName(String name) {
		
		var response = new ResponseDto();
		
		try {
			
			var dbImage = imageRepository.findByName(name);
	        
	        var dtoImage = new ImagePaintingDataDto();
	        
	        BeanUtils.copyProperties(dbImage, dtoImage);
	        
	        response.setPayload(dtoImage);
	        
		} catch(Exception e) {
			
			ErrorDto error = new ErrorDto();
			error.setMsg("Errore nell'upload dell'immagine del quadro!");
		}
				
		return response;
    }

//    @Transactional
//    public byte[] getImage(String name) {
//        Optional<ImageData> dbImage = imageRepository.findByName(name);
//        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
//        return image;
//        var response = new ResponseDto();
//			
//		try {
//			
//			var dbImage = imageRepository.findByName(name);
//	        
//	        var dtoImage = new ImagePaintingDataDto();
//	        
//	        BeanUtils.copyProperties(dbImage, dtoImage);
//	        
//	        response.setPayload(dtoImage);
//	        
//		} catch(Exception e) {
//			
//			ErrorDto error = new ErrorDto();
//			error.setMsg("Errore nell'upload dell'immagine del quadro!");
//		}
//				
//		return response;
//    }
	
	// INTERNAL METHODS
	public static byte[] compressImage(byte[] data) {

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception e) {
        }
        return outputStream.toByteArray();
    }
	
	public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception exception) {
        }
        return outputStream.toByteArray();
    }
}
