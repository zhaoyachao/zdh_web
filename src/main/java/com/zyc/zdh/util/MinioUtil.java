package com.zyc.zdh.util;

import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class MinioUtil {


    /**
     * 创建client
     * @param ak
     * @param sk
     * @param endpoint
     * @return
     */
    public static MinioClient buildMinioClient(String ak, String sk, String endpoint){
        try{
            return MinioClient.builder().endpoint(endpoint).credentials(ak, sk).build();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 检查bucket是否存在
     * @param minioClient
     * @param bucket
     * @param region
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public static boolean bucketExists(MinioClient minioClient, String bucket, String region) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {

        try{
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucket).region(region).build();
            return minioClient.bucketExists(bucketExistsArgs);
        }catch (Exception e){
            throw e;
        }
    }


    /**
     * 创建bucket
     * @param minioClient
     * @param bucket
     * @param region
     * @throws IOException
     * @throws InvalidResponseException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws ErrorResponseException
     * @throws XmlParserException
     * @throws InsufficientDataException
     * @throws InternalException
     */
    public static void createBucket(MinioClient minioClient, String bucket, String region) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InsufficientDataException, InternalException {
        try{
            if(!bucketExists(minioClient, bucket, region)){
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucket).region(region).build();
                minioClient.makeBucket(makeBucketArgs);
            }
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 删除bucket
     * @param minioClient
     * @param bucket
     * @param region
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public static void removeBucket(MinioClient minioClient, String bucket, String region) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {

        try{
            if(bucketExists(minioClient, bucket, region)){
                RemoveBucketArgs removeBucketArgs = RemoveBucketArgs.builder().bucket(bucket).region(region).build();
                minioClient.removeBucket(removeBucketArgs);
            }
        }catch (Exception e){
            throw e;
        }

    }


    /**
     * 上传本地文件到对象
     * @param minioClient
     * @param bucket
     * @param region
     * @param contentType
     * @param objectName
     * @param fileName
     * @param tags
     */
    public static void putObject(MinioClient minioClient, String bucket, String region, String contentType, String objectName, String fileName, Map<String, String> tags) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        try{
            if(!bucketExists(minioClient, bucket, region)){
                createBucket(minioClient, bucket, region);
            }
            UploadObjectArgs.Builder builder = UploadObjectArgs.builder().bucket(bucket).region(region).contentType(contentType).filename(fileName).object(objectName);
            if(tags != null){
                builder = builder.tags(tags);
            }
            UploadObjectArgs uploadObjectArgs = builder.build();
            minioClient.uploadObject(uploadObjectArgs);

        }catch (Exception e){
            throw e;
        }
    }


    /**
     * 上传输入流到对象
     * @param minioClient
     * @param bucket
     * @param region
     * @param contentType
     * @param objectName
     * @param inputStream
     * @param tags
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public static void putObject(MinioClient minioClient, String bucket, String region, String contentType, String objectName, InputStream inputStream, Map<String, String> tags) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        try{
            PutObjectArgs.Builder builder = PutObjectArgs.builder().bucket(bucket).region(region).
                    contentType(contentType).object(objectName).stream(inputStream, inputStream.available(), -1);
            if(tags != null){
                builder = builder.tags(tags);
            }
            PutObjectArgs putObjectArgs = builder.build();
            minioClient.putObject(putObjectArgs);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 删除对象
     * @param minioClient
     * @param bucket
     * @param region
     * @param objectName
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public static void removeObject(MinioClient minioClient, String bucket, String region, String objectName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        try{
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucket).region(region).object(objectName).build();
            minioClient.removeObject(removeObjectArgs);
        }catch (Exception e){
            throw e;
        }
    }


    /**
     * 获取对象
     * @param minioClient
     * @param bucket
     * @param region
     * @param objectName
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public static GetObjectResponse getObject(MinioClient minioClient, String bucket, String region, String objectName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        try{
            GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).region(region).object(objectName).build();
            GetObjectResponse getObjectResponse = minioClient.getObject(getObjectArgs);
            return getObjectResponse;
        }catch (Exception e){
            throw e;
        }
    }

}
