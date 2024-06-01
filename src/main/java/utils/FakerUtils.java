package utils;

import com.github.javafaker.Faker;

public class FakerUtils {
    public Long generateRandomumber(){
        Faker faker = new Faker();
        System.out.println(faker.beer().name());
        System.out.println(faker.aquaTeenHungerForce().character());
        System.out.println(faker.chuckNorris().fact());

        return faker.number().randomNumber();

    }
}
