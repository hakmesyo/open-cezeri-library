/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.djl.examples.denemeler;

import ai.djl.Application;
import ai.djl.repository.Artifact;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 *
 * @author cezerilab
 */
public class PrintOutZooModels {

    public static void main(String[] args) throws IOException, ModelNotFoundException {

        Map<Application,List<Artifact>> models= ModelZoo.listModels();
        models.forEach(
                (app,list) -> {
                    String appName=app.toString();
                    list.forEach(artifact -> System.out.println(appName+" "+artifact));
                }
        );
    }
}
