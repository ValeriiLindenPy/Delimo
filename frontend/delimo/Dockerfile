# Build step
FROM node:lts-alpine AS build
WORKDIR /app
COPY . .
RUN npm ci --include=dev
RUN npm run build

# Production step
FROM nginx:stable-alpine AS production
COPY --from=build /app/dist /usr/share/nginx/html

# ADD CUSTOM NGINX CONFIG
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
