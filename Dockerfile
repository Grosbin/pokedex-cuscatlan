# Multi-stage Docker build for cross-platform Pokémon Cuscatlán (Android)
# Note: This builds the Android APK. iOS requires Xcode and can't be built in Docker.
FROM openjdk:17-jdk-slim AS build

# Install Android SDK
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

# Install necessary packages
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Download and install Android SDK
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O /tmp/cmdline-tools.zip && \
    unzip /tmp/cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm /tmp/cmdline-tools.zip

# Accept licenses and install required SDK components
RUN yes | sdkmanager --licenses && \
    sdkmanager "platforms;android-35" \
               "build-tools;35.0.0" \
               "platform-tools"

# Set working directory
WORKDIR /app

# Copy project files
COPY android/ .

# Grant execute permission to gradlew
RUN chmod +x ./gradlew

# Build the APK
RUN ./gradlew assembleRelease

# Production stage - serve the APK via nginx
FROM nginx:alpine AS production

# Copy built APK to nginx
COPY --from=build /app/app/build/outputs/apk/release/app-release.apk /usr/share/nginx/html/

# Create a simple index.html for download
RUN echo '<!DOCTYPE html>' > /usr/share/nginx/html/index.html && \
    echo '<html><head><title>Pokémon Cuscatlán - Cross Platform</title><meta charset="UTF-8"></head>' >> /usr/share/nginx/html/index.html && \
    echo '<body style="font-family: Arial, sans-serif; text-align: center; padding: 50px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">' >> /usr/share/nginx/html/index.html && \
    echo '<div style="background: white; border-radius: 20px; padding: 40px; max-width: 600px; margin: 0 auto; box-shadow: 0 10px 30px rgba(0,0,0,0.2);">' >> /usr/share/nginx/html/index.html && \
    echo '<h1 style="color: #333; margin-bottom: 20px;">🔥 Pokémon Cuscatlán</h1>' >> /usr/share/nginx/html/index.html && \
    echo '<p style="color: #666; font-size: 18px;">Cross-platform mobile app for Pokemon trainers</p>' >> /usr/share/nginx/html/index.html && \
    echo '<div style="margin: 30px 0;">' >> /usr/share/nginx/html/index.html && \
    echo '<h3 style="color: #333;">📱 Available Platforms:</h3>' >> /usr/share/nginx/html/index.html && \
    echo '<p style="color: #666;">🤖 <strong>Android</strong>: Download APK below</p>' >> /usr/share/nginx/html/index.html && \
    echo '<p style="color: #666;">🍎 <strong>iOS</strong>: Build from source with Xcode</p>' >> /usr/share/nginx/html/index.html && \
    echo '</div>' >> /usr/share/nginx/html/index.html && \
    echo '<a href="/app-release.apk" download style="background: linear-gradient(135deg, #ff6b6b, #ffa500); color: white; padding: 15px 30px; text-decoration: none; border-radius: 25px; display: inline-block; margin: 20px; font-weight: bold; box-shadow: 0 5px 15px rgba(255,107,107,0.3);">📱 Download Android APK</a>' >> /usr/share/nginx/html/index.html && \
    echo '<p style="color: #999; font-size: 14px; margin-top: 30px;">Built with ❤️ using Kotlin & Jetpack Compose + Swift & SwiftUI</p>' >> /usr/share/nginx/html/index.html && \
    echo '</div></body></html>' >> /usr/share/nginx/html/index.html

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget -q --spider http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]