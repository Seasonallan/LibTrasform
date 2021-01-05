package com.example.form;


import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.AppExtension;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;
import com.example.testplugin.MethodTotal;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;


public class TestTransform extends Transform implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().getByType(AppExtension.class).registerTransform(this);
    }

    @Override
    public String getName() {
        return "TestTransform2021";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation invocation) {
        System.out.println("TestTransform transform");
        for (TransformInput input : invocation.getInputs()) {
            //遍历jar文件 对jar不操作，但是要输出到out路径
            input.getJarInputs().parallelStream().forEach(jarInput -> {
                File src = jarInput.getFile();
                System.out.println("input.getJarInputs :" + src.toString());
                File dst = invocation.getOutputProvider().getContentLocation(
                        jarInput.getName(), jarInput.getContentTypes(), jarInput.getScopes(),
                        Format.JAR);
                try {
                    FileUtils.copyFile(src, dst);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            input.getDirectoryInputs().parallelStream().forEach(directoryInput -> {
                File src = directoryInput.getFile();
                System.out.println("input.getDirectoryInputs :" + src.toString());
                try {
                    //scanFilesAndInsertCode(src.getAbsolutePath());
                    //scanFilesAndInsertCode2(src.getAbsolutePath());
                    eachFile(src);

                    File dst = invocation.getOutputProvider().getContentLocation(
                            directoryInput.getName(), directoryInput.getContentTypes(),
                            directoryInput.getScopes(), Format.DIRECTORY);
                    FileUtils.copyDirectory(src, dst);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    private void eachFile(File src) {
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (int i = 0; i < files.length; i++) {
                File itemFile = files[i];
                eachFile(itemFile);
            }
        } else {
            try {
                System.out.println("input.getDirectoryInputs :" + src.toString());
                String fileName = src.getName();
                if (fileName.endsWith(".class") && fileName != "BuildConfig.class" && fileName != "R.class") {
                    FileInputStream fis = new FileInputStream(src);
                    ClassReader cr = new ClassReader(fis);
                    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                    MethodTotal classVisitor = new MethodTotal(Opcodes.ASM5, cw);
                    cr.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                    byte[] bytes = cw.toByteArray();
                    //写回原来这个类所在的路径
                    FileOutputStream fos = new FileOutputStream(src.getParentFile().getAbsolutePath()
                            + File.separator + src.getName());
                    fos.write(bytes);
                    fos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

//
//
//    private void scanFilesAndInsertCode(String path) throws Exception {
//        ClassPool classPool = ClassPool.getDefault();
//        classPool.appendClassPath(path);
//        CtClass ctClass = classPool.getCtClass("com.example.testplugin.PluginTestClass");
//        if (ctClass == null) {
//            return;
//        }
//        if (ctClass.isFrozen()) {
//            ctClass.defrost();
//        }
//        CtMethod ctMethod = ctClass.getDeclaredMethod("init");
//
//        String insetStr = "System.out.println(\"after2021\");";
//        //  ctMethod.insertBefore("android.widget.Toast.makeText(this,\"我是被插入的Toast代码~!!\",android.widget.Toast.LENGTH_SHORT).show();");
//        // String toastStr = 'android.widget.Toast.makeText(this,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show();';
//        ctMethod.insertAfter(insetStr);
//        // ctMethod.insertAfter(toastStr);
//        ctClass.writeFile(path);
//        ctClass.detach();//释放
//    }
//
//    private void scanFilesAndInsertCode2(String path) throws Exception {
//        if (true) {
//
//            return;
//        }
//        ClassPool classPool = ClassPool.getDefault();
//        classPool.appendClassPath(path);
//        classPool.importPackage("android.os.Bundle");
//        CtClass ctClass = classPool.getCtClass("com.example.testplugin.MainActivity");
//        if (ctClass == null) {
//            return;
//        }
//        if (ctClass.isFrozen()) {
//            ctClass.defrost();
//        }
//        CtMethod ctMethod = ctClass.getDeclaredMethod("onNext");
//
//        String insetStr = "System.out.println(\"ONCREATE\");";
//        // ctMethod.insertBefore("System.out.println(\"END\");");
//        String toastStr = "android.widget.Toast.makeText(this,\"我是被插入的Toast代码~!!\",android.widget.Toast.LENGTH_SHORT).show();";
//        ctMethod.insertAfter(insetStr);
//        // ctMethod.insertAfter(toastStr);
//        ctClass.writeFile(path);
//        ctClass.detach();//释放
//    }
}
