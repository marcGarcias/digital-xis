FROM node:22-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm ci

COPY . .

RUN npm run build

# =========================

FROM node:22-alpine AS production

WORKDIR /app

ENV NODE_ENV=production
ENV PORT=4200

COPY --from=build /app/dist/front-end ./dist/front-end

EXPOSE 4200

CMD ["node", "dist/front-end/server/server.mjs"]
