package fr.mossaab.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Конфигурационный класс SecurityConfiguration для настройки Spring Security.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Фильтр аутентификации по JWT
    private final AuthenticationProvider authenticationProvider; // Провайдер аутентификации
    private final Http401UnauthorizedEntryPoint unauthorizedEntryPoint; // Обработчик неаутентифицированных запросов
    private final CustomAccessDeniedHandler accessDeniedHandler; // Обработчик запрета доступа

    private static final Long MAX_AGE = 3600L; // Максимальное время жизни CORS-заголовков
    private static final int CORS_FILTER_ORDER = -102; // Порядок CORS-фильтра

    /**
     * Конфигурация цепочки фильтров безопасности.
     *
     * @param http Объект HttpSecurity для настройки
     * @return Цепочка фильтров безопасности
     * @throws Exception Исключение, выбрасываемое при настройке
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable) // Отключение CORS
                .csrf(AbstractHttpConfigurer::disable) // Отключение CSRF
                .exceptionHandling(exception -> exception
                        /*.authenticationEntryPoint(unauthorizedEntryPoint)*/
                        .accessDeniedHandler(accessDeniedHandler)) // Настройка обработчика ошибок
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/passport/**",
                                        "/heatingSystem/**",
                                        "/authentication/**",
                                        "/user/**",
                                        "/admin/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/types/**",
                                        "/kinds/**",
                                        "/documents/**",
                                        "/service-centers/**",
                                        "/bonus-program/**",
                                        "/series/**",
                                        "/unit/**",
                                        "/characteristic/**",
                                        "/attribute/**",
                                        "/error/**",
                                        "/type/**",
                                        "/kind/**",
                                        "/value/**",
                                        "/boiler/**",
                                        "/advantage/**",
                                        "/spare-part/**",
                                        "/boiler-series-passport/**",
                                        "/explosion-diagram/**",
                                        "/messages/**"
                                ).permitAll() // Разрешение доступа к определенным ресурсам без аутентификации
                                .requestMatchers(HttpMethod.POST, "/api/v1/resource").hasRole("ADMIN") // Разрешение доступа с ролью ADMIN
                                .anyRequest().authenticated()) // Аутентификация для остальных запросов
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS)) // Установка политики управления сеансами
                .authenticationProvider(authenticationProvider) // Установка провайдера аутентификации
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Добавление фильтра аутентификации JWT
                .build();
    }

    /**
     * Конфигурация фильтра CORS.
     *
     * @return Фильтр CORS
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Разрешение использования учетных данных в запросах CORS
        config.addAllowedOrigin("http://localhost:4200"); // Разрешенный источник запросов
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT)); // Разрешенные заголовки
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name())); // Разрешенные методы HTTP
        config.setMaxAge(MAX_AGE); // Максимальное время жизни заголовков CORS
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(CORS_FILTER_ORDER); // Установка порядка фильтра
        return bean;
    }
}
